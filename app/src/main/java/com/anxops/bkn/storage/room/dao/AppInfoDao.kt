package com.anxops.bkn.storage.room.dao

import androidx.room.*
import com.anxops.bkn.storage.room.entities.AppInfo
import com.anxops.bkn.storage.room.entities.ProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppInfoDao {
    @Query("SELECT * FROM app_info LIMIT 1")
    suspend fun getAppInfo(): AppInfo?

    @Query("SELECT * FROM app_info LIMIT 1")
    fun getAppInfoFlow(): Flow<AppInfo?>

    @Insert
    suspend fun insert(info: AppInfo)

    @Query("DELETE FROM app_info")
    suspend fun clear()
}