package com.anxops.bkn.data.database.dao

import androidx.room.*
import com.anxops.bkn.data.database.entities.BikeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BikeDao {

    @Query("SELECT * FROM bike ORDER BY distance DESC")
    suspend fun findAll(): List<BikeEntity>

    @Query("SELECT * FROM bike WHERE draft == 0 ORDER BY distance DESC")
    fun flow(): Flow<List<BikeEntity>>

    @Query("SELECT * FROM bike WHERE _id = :bikeId")
    suspend fun getById(bikeId: String): BikeEntity?

    @Update
    suspend fun update(bike: BikeEntity)

    @Query("UPDATE bike SET draft = 1")
    suspend fun unSyncAllBikes()

    @Query("UPDATE bike SET draft = 0 WHERE _id IN (:ids)")
    suspend fun syncSelectedBikes(ids: Set<String>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bike: BikeEntity)

    @Query("DELETE FROM component WHERE bike_id = :bikeId")
    suspend fun removeAllBikeComponents(bikeId: String)

    @Delete
    suspend fun delete(bike: BikeEntity)

    @Query("DELETE FROM bike WHERE _id = :bikeId")
    suspend fun delete(bikeId: String)

    @Query("DELETE FROM bike")
    suspend fun clear()

    @Transaction
    suspend fun syncBikes(ids: Set<String>) {
        unSyncAllBikes()
        syncSelectedBikes(ids)
    }

}