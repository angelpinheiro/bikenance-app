package com.anxops.bkn.data.database.dao

import androidx.room.*
import com.anxops.bkn.data.database.entities.ProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    @Query("SELECT COUNT(*) FROM profile")
    suspend fun count(): Int

    suspend fun exists() = count() > 0

    @Query("SELECT * FROM profile LIMIT 1")
    suspend fun getProfile(): ProfileEntity

    @Query("SELECT * FROM profile LIMIT 1")
    fun getProfileFlow(): Flow<ProfileEntity>

    @Update
    suspend fun update(profile: ProfileEntity)

    @Insert
    suspend fun insert(profile: ProfileEntity)

    @Delete
    suspend fun delete(profile: ProfileEntity)

    @Query("DELETE FROM profile")
    suspend fun clear()
}
