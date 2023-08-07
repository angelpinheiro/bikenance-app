package com.anxops.bkn.data.repository

import androidx.room.withTransaction
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.entities.BikeEntity
import com.anxops.bkn.data.database.entities.MaintenanceEntity
import com.anxops.bkn.data.database.toEntity
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.Maintenance
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.ApiResponse
import com.anxops.bkn.data.network.successOrException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


interface BikeRepositoryFacade {

    suspend fun getBike(id: String): Result<Bike>

    suspend fun getBikes(): List<Bike>

    suspend fun updateBike(bike: Bike)

    suspend fun setupBike(bike: Bike)

    suspend fun deleteBike(bike: Bike)

    suspend fun createBike(bike: Bike)

    suspend fun updateSynchronizedBikes(ids: Map<String, Boolean>)

    fun getBikesFlow(fillComponents: Boolean = false): Flow<List<Bike>>

    suspend fun refreshBikes(): Result<Unit>

    suspend fun getBikeMaintenance(id: String): Result<Maintenance?>

    suspend fun getBikeComponent(id: String): Result<BikeComponent?>

    suspend fun updateMaintenance(bike: Bike, m: Maintenance): Result<Unit>

    suspend fun replaceComponent(it: BikeComponent): Result<BikeComponent?>
}

class BikeRepository(
    val api: Api,
    val db: AppDb,
    val ridesRepository: RidesRepositoryFacade,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BikeRepositoryFacade, BaseRepository(defaultDispatcher) {

    override suspend fun refreshBikes(): Result<Unit> = result {
        api.getBikes().successOrException { bikes ->
            db.database().withTransaction {
                db.bikeDao().clear()
                bikes.forEach {
                    insertOrUpdateBike(it)
                }
            }
        }
    }

    override suspend fun getBikeMaintenance(id: String): Result<Maintenance?> = result {
        db.maintenanceDao().getById(id)?.toDomain()
    }

    override suspend fun getBikeComponent(id: String): Result<BikeComponent?> = result {
        db.bikeComponentDao().getById(id)?.toDomain()?.let {
            val maintenances =
                db.maintenanceDao().getByComponentId(it._id).map { m -> m.toDomain() }
            it.copy(maintenances = maintenances)
        }
    }

    override suspend fun updateMaintenance(bike: Bike, m: Maintenance): Result<Unit> = result {
        api.updateMaintenance(bike, m).successOrException {
            insertOrUpdateBike(it)
        }
    }

    override suspend fun replaceComponent(c: BikeComponent): Result<BikeComponent?> = result {
        api.replaceComponent(c).successOrException { newComponentId ->
            c.bikeId?.let { refreshBike(it) }
            getBikeComponent(newComponentId).data()
        }
    }

    override suspend fun getBike(id: String): Result<Bike> = result {
        db.bikeDao().getById(id)?.let {
            getBikeWithComponentsAndMaintenances(it.toDomain())
        } ?: throw RuntimeException("Bike with id $id not found")
    }

    override suspend fun getBikes(): List<Bike> = withContext(defaultDispatcher) {
        db.bikeDao().findAll().map(BikeEntity::toDomain)
    }

    override fun getBikesFlow(fillComponents: Boolean): Flow<List<Bike>> {
        return db.bikeDao().flow().map { bikes ->
            bikes.map(BikeEntity::toDomain).map {
                if (!fillComponents) it else getBikeWithComponentsAndMaintenances(it)
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


    private suspend fun getBikeWithComponentsAndMaintenances(it: Bike): Bike {
        return withContext(defaultDispatcher) {
            it.copy(components = db.bikeComponentDao().bike(it._id).map { component ->
                val maintenances = db.maintenanceDao().getByComponentId(component.component._id)
                    .map(MaintenanceEntity::toDomain)
                component.toDomain().copy(maintenances = maintenances)
            })
        }
    }
}