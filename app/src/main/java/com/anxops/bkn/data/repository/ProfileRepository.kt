package com.anxops.bkn.data.repository

import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.toEntity
import com.anxops.bkn.data.model.AthleteStats
import com.anxops.bkn.data.model.Profile
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.ApiResponse
import com.anxops.bkn.util.RepositoryResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


interface ProfileRepositoryFacade {

    suspend fun reloadData(): Boolean

    suspend fun getProfile(): Profile?

    suspend fun getProfileStats(): AthleteStats?

    suspend fun updateProfile(profile: Profile): RepositoryResult<Profile>

    fun getProfileFlow(): Flow<Profile?>

}

class ProfileRepository(
    val api: Api, val db: AppDb, private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ProfileRepositoryFacade {

    override suspend fun reloadData(): Boolean {
        return withContext(defaultDispatcher) {
            val success = when (val profile = api.profile()) {
                is ApiResponse.Success -> {
                    db.profileDao().clear()
                    profile.data.let {
                        db.profileDao().insert(it.toEntity())
                    }
                    true
                }
                else -> false
            }
            success
        }
    }

    override suspend fun getProfile(): Profile? {
        return db.profileDao().getProfile()?.toDomain()
    }

    override suspend fun getProfileStats(): AthleteStats? = withContext(defaultDispatcher) {
        when (val profile = api.profile()) {
            is ApiResponse.Success -> {
                profile.data.stats

            }
            else -> null
        }
    }

    override fun getProfileFlow(): Flow<Profile?> {
        return db.profileDao().getProfileFlow().map {
            it?.toDomain()
        }
    }

    override suspend fun updateProfile(profile: Profile): RepositoryResult<Profile> {
        return when (val result = api.updateProfile(profile)) {

            is ApiResponse.Success -> {
                db.profileDao().update(profile.toEntity())
                RepositoryResult.Success(profile)
            }

            else -> RepositoryResult.Error(result.message ?: "Unknown error")

        }


    }

}