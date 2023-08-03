package com.anxops.bkn.data.repository

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
import timber.log.Timber


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

    suspend fun getBikeMaintenance(id: String): Maintenance?

    suspend fun getBikeComponent(id: String): BikeComponent?

    suspend fun updateMaintenance(bike: Bike, m: Maintenance)

    suspend fun replaceComponent(it: BikeComponent): BikeComponent?
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
                    db.bikeDao().clear()
                    apiResponse.data.forEach {
                        insertOrUpdateBike(it)
                    }
                }
                true
            }
            else -> false
        }
    }

    override suspend fun getBikeMaintenance(id: String): Maintenance? =
        withContext(defaultDispatcher) {
            db.maintenanceDao().getById(id)?.toDomain()
        }

    override suspend fun getBikeComponent(id: String): BikeComponent? =
        withContext(defaultDispatcher) {
            db.bikeComponentDao().getById(id)?.toDomain()?.let {
                val maintenances =
                    db.maintenanceDao().getByComponentId(it._id).map { m -> m.toDomain() }
                it.copy(maintenances = maintenances)
            }
        }

    override suspend fun updateMaintenance(bike: Bike, m: Maintenance) =
        withContext(defaultDispatcher) {
            when (val response = api.updateMaintenance(bike, m)) {
                is ApiResponse.Success -> {
                    insertOrUpdateBike(response.data)
                }

                else -> {
                    throw Exception("Could not updateSynchronizedBikes")
                }
            }
        }

    override suspend fun replaceComponent(c: BikeComponent): BikeComponent? {
        return withContext(defaultDispatcher) {
            when (val response = api.replaceComponent(c)) {
                is ApiResponse.Success -> {
                    c.bikeId?.let { refreshBike(it) }
                    getBikeComponent(response.data)
                }

                else -> {
                    throw Exception("Could not replace component")
                }
            }
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
                insertOrUpdateBike(response.data)
            }
        }
    }

    private suspend fun insertOrUpdateBike(bike: Bike) {
        db.database().withTransaction {
            // remove old bike components
            db.bikeDao().removeAllBikeComponents(bike._id)
            // save new bike data
            db.bikeDao().insert(bike.toEntity())
            // save new components and maintenances
            bike.components?.forEach { component ->
                db.bikeComponentDao().insert(component.toEntity())
                component.maintenances?.forEach {
                    db.maintenanceDao().insert(it.toEntity())
                }
            }
        }
    }

    private suspend fun refreshBike(
        bikeId: String
    ) {
        when (val response = api.getBike(bikeId)) {
            is ApiResponse.Error -> throw Exception(response.message)
            is ApiResponse.Success -> {
                insertOrUpdateBike(response.data)
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