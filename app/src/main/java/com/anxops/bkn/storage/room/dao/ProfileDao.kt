package com.anxops.bkn.storage.room.dao

import androidx.room.*
import com.anxops.bkn.storage.room.entities.ProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    @Query("SELECT * FROM profile LIMIT 1")
    suspend fun getProfile(): ProfileEntity?

    @Query("SELECT * FROM profile LIMIT 1")
    fun getProfileFlow(): Flow<ProfileEntity?>

    @Update
    suspend fun update(profile: ProfileEntity)

    @Insert
    suspend fun insert(profile: ProfileEntity)

    @Delete
    suspend fun delete(profile: ProfileEntity)

    @Query("DELETE FROM profile")
    suspend fun clear()
}