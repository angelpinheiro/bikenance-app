package com.anxops.bkn.storage.room.dao

import androidx.room.*
import com.anxops.bkn.storage.room.entities.BikeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BikeDao {

    @Query("SELECT * FROM bike ORDER BY distance DESC")
    suspend fun findAll(): List<BikeEntity>

    @Query("SELECT * FROM bike ORDER BY distance DESC")
    fun flow(): Flow<List<BikeEntity>>

    @Query("SELECT * FROM bike WHERE _id = :bikeId")
    suspend fun getById(bikeId: String): BikeEntity?

    @Update
    suspend fun update(bike: BikeEntity)

    @Query("UPDATE bike SET draft = 1")
    suspend fun unSyncAllBikes()

    @Query("UPDATE bike SET draft = 0 WHERE _id IN (:ids)")
    suspend fun syncSelectedBikes(ids: List<String>)

    @Transaction
    suspend fun syncBikes(ids: List<String>) {
        unSyncAllBikes()
        syncSelectedBikes(ids)
    }

    @Insert
    suspend fun insert(bike: BikeEntity)

    @Insert
    suspend fun insertAll(vararg bikes: BikeEntity)

    @Delete
    suspend fun delete(bike: BikeEntity)

    @Query("DELETE FROM bike")
    suspend fun clear()

}