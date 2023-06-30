package com.anxops.bkn.storage.room.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.anxops.bkn.storage.room.entities.BikeRideEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface BikeRideDao {

    @Query("SELECT * FROM bike_ride WHERE _id = :rideId")
    suspend fun getById(rideId: String): BikeRideEntity?

    @Query("SELECT * FROM bike_ride ORDER BY date_time DESC")
    fun findAll(): List<BikeRideEntity>

    @Query("SELECT * FROM bike_ride ORDER BY date_time DESC")
    fun flow(): Flow<List<BikeRideEntity>>

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