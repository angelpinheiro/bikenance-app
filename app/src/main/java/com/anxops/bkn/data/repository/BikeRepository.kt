package com.anxops.bkn.data.repository

import android.util.Log
import androidx.room.withTransaction
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.entities.BikeEntity
import com.anxops.bkn.data.database.toEntity
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.Maintenance
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.ApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


interface BikeRepositoryFacade {

    suspend fun getBike(id: String): Bike?

    suspend fun getBikes(): List<Bike>

    suspend fun updateBike(bike: Bike)

    suspend fun setupBike(bike: Bike)

    suspend fun deleteBike(bike: Bike)

    suspend fun createBike(bike: Bike)

    suspend fun updateSynchronizedBikes(ids: Map<String, Boolean>)

    fun getBikesFlow(full: Boolean = false): Flow<List<Bike>>

    suspend fun refreshBikes(): Boolean

    suspend fun getBikeMaintenance(id: String) : Maintenance?

    suspend fun getBikeComponent(id: String) : BikeComponent?
}

class BikeRepository(
    val api: Api,
    val db: AppDb,
    val ridesRepository: RidesRepositoryFacade,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BikeRepositoryFacade {

    override suspend fun refreshBikes(): Boolean = withContext(defaultDispatcher) {

        when (val apiResponse = api.getBikes()) {
            is ApiResponse.Success -> {
                db.database().withTransaction {
                    val bikes = apiResponse.data

                    val bikeEntities = bikes.map { it.toEntity() }
                    val componentEntities = bikes.flatMap { bike ->
                        bike.components?.map { it.toEntity() } ?: emptyList()
                    }
                    val maintenanceEntities = bikes.flatMap { bike ->
                        bike.components?.flatMap {
                            it.maintenances?.map { m -> m.toEntity() } ?: listOf()
                        } ?: emptyList()
                    }

                    db.bikeDao().clear()
                    db.bikeComponentDao().clear()
                    db.maintenanceDao().clear()

                    db.bikeDao().insertAll(bikeEntities)
                    db.bikeComponentDao().insertAll(componentEntities)
                    db.maintenanceDao().insertAll(maintenanceEntities)
                }
                true
            }

            else -> false
        }
    }

    override suspend fun getBikeMaintenance(id: String): Maintenance? = withContext(defaultDispatcher) {
        db.maintenanceDao().getById(id)?.toDomain()
    }

    override suspend fun getBikeComponent(id: String): BikeComponent? = withContext(defaultDispatcher) {
        db.bikeComponentDao().getById(id)?.toDomain()?.let {
            val maintenances = db.maintenanceDao().getByComponentId(it._id).map { m -> m.toDomain() }
            it.copy(maintenances = maintenances)
        }
    }

    override suspend fun getBike(id: String): Bike? = withContext(defaultDispatcher) {

        db.bikeDao().getById(id)?.let {
            it.toDomain().copy(components = db.bikeComponentDao().bike(id).map { c ->
                val maintenances = db.maintenanceDao().getByComponentId(c.component._id).map { m ->
                    m.toDomain()
                }
                c.toDomain().copy(maintenances = maintenances)
            })
        }
    }


    override suspend fun getBikes(): List<Bike> = withContext(defaultDispatcher) {
        db.bikeDao().findAll().map(BikeEntity::toDomain)
    }

    override fun getBikesFlow(full: Boolean): Flow<List<Bike>> {
        return db.bikeDao().flow().map { bikes ->
            withContext(defaultDispatcher) {
                if (full) bikes.map {

                    it.toDomain().copy(components = db.bikeComponentDao().bike(it._id).map { c ->
                        val maintenances =
                            db.maintenanceDao().getByComponentId(c.component._id).map { m ->
                                m.toDomain()
                            }

                        c.toDomain().copy(maintenances = maintenances)
                    })
                }
                else {
                    bikes.map {
                        it.toDomain()
                    }
                }
            }
        }
    }

    override suspend fun updateBike(bike: Bike) {
        db.bikeDao().update(bike.toEntity())
        api.updateBike(bike)
    }

    override suspend fun setupBike(bike: Bike) {
        when (val response = api.setupBike(bike)) {
            is ApiResponse.Error -> throw Exception(response.message)
            is ApiResponse.Success -> {
                db.database().withTransaction {
                    db.bikeDao().update(response.data.toEntity())
                    db.bikeComponentDao().clearBike(bike._id)
                    response.data.components?.forEach { component ->
                        db.bikeComponentDao().insert(component.toEntity())
                        component.maintenances?.forEach {
                            db.maintenanceDao().insert(it.toEntity())
                        }
                    }
                }
            }
        }
    }

    override suspend fun deleteBike(bike: Bike) {
        db.bikeDao().delete(bike.toEntity())
        api.deleteBike(bike)
    }

    override suspend fun createBike(bike: Bike) {
        db.bikeDao().insert(bike.toEntity())
        api.createBike(bike)
    }

    override suspend fun updateSynchronizedBikes(ids: Map<String, Boolean>) {
        when (api.syncBikes(ids)) {
            is ApiResponse.Success -> {
                refreshBikes()
            }

            else -> {
                throw Exception("Could not updateSynchronizedBikes")
            }
        }
    }
}