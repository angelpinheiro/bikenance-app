package com.anxops.bkn.storage

import com.anxops.bkn.model.Bike
import com.anxops.bkn.model.BikeRide
import com.anxops.bkn.model.Profile
import kotlinx.coroutines.flow.Flow

interface Syncable {

    fun sync() {}

}

interface BikeRepositoryFacade {

    suspend fun getBike(id: String): Bike?

    suspend fun getBikes(): List<Bike>

    suspend fun updateBike(bike: Bike)

    suspend fun deleteBike(bike: Bike)

    suspend fun createBike(bike: Bike)

    suspend fun updateSynchronizedBikes(ids: List<String>)

    fun getBikesFlow(draft: Boolean = false): Flow<List<Bike>>

    suspend fun reloadData(): Boolean
}

interface RidesRepositoryFacade {

    suspend fun getRide(id: String): BikeRide?

    suspend fun getRides(): List<BikeRide>

    fun getRidesFlow(): Flow<List<BikeRide>>

    suspend fun updateRide(ride: BikeRide)

    suspend fun reloadData(): Boolean
}

interface ProfileRepositoryFacade {

    suspend fun reloadData(): Boolean

    suspend fun getProfile(): Profile?

    suspend fun updateProfile(profile: Profile)

    fun getProfileFlow(): Flow<Profile?>

}