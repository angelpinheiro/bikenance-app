package com.anxops.bkn.data.database.dao

import androidx.room.*
import com.anxops.bkn.data.database.entities.MaintenanceEntity


@Dao
interface MaintenanceDao {
    @Query("SELECT * FROM maintenance WHERE _id = :id")
    suspend fun getById(id: String): MaintenanceEntity?

    @Query("SELECT * FROM maintenance WHERE componentId = :id")
    suspend fun getByComponentId(id: String): List<MaintenanceEntity>

    @Insert
    suspend fun insert(maintenanceEntity: MaintenanceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertAll(maintenances: List<MaintenanceEntity>)

    @Query("DELETE FROM maintenance")
    suspend fun clear()
}