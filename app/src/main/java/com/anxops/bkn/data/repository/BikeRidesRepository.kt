package com.anxops.bkn.data.repository

import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.entities.BikeRideEntity
import com.anxops.bkn.data.database.toEntity
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.successOrException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface RidesRepositoryFacade {

    suspend fun getRide(id: String): Result<BikeRide?>

    suspend fun getRides(): Result<List<BikeRide>>

    suspend fun getLastRides(bikeId: String): Result<List<BikeRide>>

    suspend fun updateRide(ride: BikeRide): Result<BikeRide>

    suspend fun refreshRides(): Result<Unit>
}

class BikeRidesRepository(
    val api: Api, val db: AppDb, private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RidesRepositoryFacade, BaseRepository(defaultDispatcher) {


    override suspend fun refreshRides(): Result<Unit> = result {
        api.getRides().successOrException {
            it.forEach { b ->
                db.bikeRideDao().insert(b.toEntity())
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

    override suspend fun getRide(id: String): Result<BikeRide?> = result {
        db.bikeRideDao().getById(id)?.toDomain()
    }

    override suspend fun getRides(): Result<List<BikeRide>> = result {
        db.bikeRideDao().findAll().map(BikeRideEntity::toDomain)
    }
}