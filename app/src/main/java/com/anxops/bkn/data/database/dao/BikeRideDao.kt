package com.anxops.bkn.data.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.anxops.bkn.data.database.entities.BikeRideEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface BikeRideDao {

    @Query("SELECT * FROM bike_ride WHERE _id = :rideId")
    suspend fun getById(rideId: String): BikeRideEntity?

    @Query("SELECT * FROM bike_ride ORDER BY date_time DESC")
    fun findAll(): List<BikeRideEntity>

    @Query("SELECT * FROM bike_ride ORDER BY date_time DESC")
    fun flow(): Flow<List<BikeRideEntity>>

    @Query("SELECT * FROM bike_ride WHERE bike_id = :bikeId ORDER BY date_time DESC")
    fun bikeFlow(bikeId: String): Flow<List<BikeRideEntity>>

    @Query("SELECT * FROM bike_ride WHERE bike_id = :bikeId ORDER BY date_time DESC LIMIT :limit")
    fun lastBikeRides(bikeId: String, limit: Int = 3): List<BikeRideEntity>

    @Query("SELECT * FROM bike_ride")
    fun pagingSource(): PagingSource<Int, BikeRideEntity>

    @Insert
    suspend fun insert(ride: BikeRideEntity)

    @Update
    suspend fun update(ride: BikeRideEntity)

    @Insert
    fun insertAll(vararg rides: BikeRideEntity)

    @Query("DELETE FROM bike_ride")
    suspend fun clear()


}