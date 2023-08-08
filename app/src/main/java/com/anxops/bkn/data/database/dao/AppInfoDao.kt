package com.anxops.bkn.data.database.dao

import androidx.room.*
import com.anxops.bkn.data.database.entities.AppInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface AppInfoDao {
    @Query("SELECT * FROM app_info LIMIT 1")
    suspend fun getAppInfo(): AppInfo?

    @Query("SELECT * FROM app_info LIMIT 1")
    fun getAppInfoFlow(): Flow<AppInfo?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(info: AppInfo)

    @Query("DELETE FROM app_info")
    suspend fun clear()
}
