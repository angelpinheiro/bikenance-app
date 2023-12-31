package com.anxops.bkn.data.repository

import androidx.room.withTransaction
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.entities.BikeRideEntity
import com.anxops.bkn.data.database.toEntity
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.successOrException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

interface RidesRepositoryFacade {

    suspend fun getRide(id: String): Result<BikeRide>

    suspend fun getRides(): Result<List<BikeRide>>

    suspend fun getLastRides(bikeId: String): Result<List<BikeRide>>

    suspend fun updateRide(ride: BikeRide): Result<BikeRide>

    suspend fun refreshRides(): Result<Unit>
}

class BikeRidesRepository(
    val api: Api,
    val db: AppDb,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RidesRepositoryFacade, BaseRepository(defaultDispatcher) {

    override suspend fun refreshRides(): Result<Unit> = result {
        api.getRides().successOrException {
            db.database().withTransaction {
                db.bikeRideDao().clear()
                it.forEach { b ->
                    try {
                        db.bikeRideDao().insert(b.toEntity())
                    } catch (e: Exception) {
                        Timber.e("An error occurred inserting: $b")
                        Timber.e(e)
                    }
                }
            }
        }
    }

    override suspend fun getLastRides(bikeId: String): Result<List<BikeRide>> = result {
        db.bikeRideDao().lastBikeRides(bikeId).map {
            it.toDomain()
        }
    }

    override suspend fun updateRide(ride: BikeRide): Result<BikeRide> = result {
        api.updateRide(ride).successOrException {
            db.bikeRideDao().update(it.toEntity())
            it
        }
    }

    override suspend fun getRide(id: String): Result<BikeRide> = result {
        db.bikeRideDao().getById(id)?.toDomain() ?: throw RuntimeException("Ride with id $id not found")
    }

    override suspend fun getRides(): Result<List<BikeRide>> = result {
        db.bikeRideDao().findAll().map(BikeRideEntity::toDomain)
    }
}
