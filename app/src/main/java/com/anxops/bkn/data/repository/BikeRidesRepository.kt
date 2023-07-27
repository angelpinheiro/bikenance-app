package com.anxops.bkn.data.repository

import android.util.Log
import androidx.room.withTransaction
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.toEntity
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.ApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface RidesRepositoryFacade {

    suspend fun getRide(id: String): BikeRide?

    suspend fun getRides(): List<BikeRide>

    suspend fun getLastBikeRides(bikeId: String): List<BikeRide>

    suspend fun updateRide(ride: BikeRide)

    suspend fun reloadData(): Boolean
}

class BikeRidesRepository(
    val api: Api, val db: AppDb, private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RidesRepositoryFacade {

    override suspend fun reloadData(): Boolean = withContext(defaultDispatcher) {
        when (val rides = api.getRides()) {
            is ApiResponse.Success -> {
                db.bikeRideDao().clear()
                rides.data.let {
                    it.forEach { b ->
                        db.bikeRideDao().insert(b.toEntity())
                    }
                }
                true
            }

            else -> false
        }
    }

    override suspend fun getLastBikeRides(bikeId: String): List<BikeRide> =
        withContext(defaultDispatcher) {
            db.bikeRideDao().lastBikeRides(bikeId).map {
                it.toDomain()
            }
        }

    override suspend fun updateRide(ride: BikeRide) {
        withContext(defaultDispatcher) {
            when (val response = api.updateRide(ride)) {
                is ApiResponse.Error -> throw Exception(response.message)
                is ApiResponse.Success -> {
                    db.bikeRideDao().update(response.data.toEntity())
                }
            }
        }
    }

    override suspend fun getRide(id: String): BikeRide? = withContext(defaultDispatcher) {
        db.bikeRideDao().getById(id)?.toDomain()
    }

    override suspend fun getRides(): List<BikeRide> = withContext(defaultDispatcher) {
        db.bikeRideDao().findAll().map { it.toDomain() }
    }

}