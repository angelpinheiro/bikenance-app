package com.anxops.bkn.data.repository

import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.toEntity
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface RidesRepositoryFacade {

    suspend fun getRide(id: String): BikeRide?

    suspend fun getRides(): List<BikeRide>

    fun getRidesFlow(): Flow<List<BikeRide>>

    suspend fun updateRide(ride: BikeRide)

    suspend fun reloadData(): Boolean
}

class BikeRidesRepository(val api: Api, val db: AppDb) : RidesRepositoryFacade {

    override suspend fun reloadData(): Boolean {

        val success = when (val rides = api.getRides()) {
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

        return success

    }

    override fun getRidesFlow(): Flow<List<BikeRide>> {
        return db.bikeRideDao().flow().map {
            it.map { b ->
                b.toDomain()
            }
        }
    }

    override suspend fun updateRide(ride: BikeRide) {
        db.bikeRideDao().update(ride.toEntity())
        api.updateRide(ride)
    }

    override suspend fun getRide(id: String): BikeRide? {
        return db.bikeRideDao().getById(id)?.toDomain()
    }

    override suspend fun getRides(): List<BikeRide> {
        return db.bikeRideDao().findAll().map { it.toDomain() }
    }

}