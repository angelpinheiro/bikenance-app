package com.anxops.bkn.data.repository

import androidx.room.withTransaction
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.toEntity
import com.anxops.bkn.data.model.AthleteStats
import com.anxops.bkn.data.model.Profile
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.successOrException
import com.anxops.bkn.data.preferences.BknDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


interface ProfileRepositoryFacade {

    suspend fun refreshProfile(): Result<Profile>

    suspend fun checkLogin(): Result<CheckLoginResult>

    suspend fun profileExists(): Result<Boolean>

    suspend fun getProfile(): Result<Profile?>

    suspend fun getProfileStats(): Result<AthleteStats?>

    suspend fun updateProfile(profile: Profile): Result<Profile>

    fun getProfileFlow(): Flow<Result<Profile?>>

    suspend fun saveLogin(token: String, refreshToken: String?): Result<Profile>

    suspend fun logout(): Result<Unit>

}

class ProfileRepository(
    val api: Api,
    val db: AppDb,
    val dataStore: BknDataStore,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ProfileRepositoryFacade, BaseRepository(defaultDispatcher) {

    override suspend fun refreshProfile(): Result<Profile> = result {
        api.profile().successOrException {
            db.profileDao().clear()
            db.profileDao().insert(it.toEntity())
            it
        }
    }

    override suspend fun checkLogin(): Result<CheckLoginResult> = result {

        val token = dataStore.authToken.firstOrNull()
        val profileExists = db.profileDao().exists()

        when (token) {
            // There is no auth token
            null -> {
                // If a profile is saved, the login has expired and the auth token deleted
                if (profileExists) {
                    CheckLoginResult.LoginExpired
                } else {
                    // If a profile dos not exist, there is no logged user
                    CheckLoginResult.NotLoggedIn
                }
            }
            // If there is an auth token and a profile exists, the user is logged in
            else -> {
                CheckLoginResult.LoggedIn(db.profileDao().getProfile().toDomain())
            }
        }
    }

    override suspend fun saveLogin(token: String, refreshToken: String?): Result<Profile> = result {
        // save auth tokens
        dataStore.saveAuthTokens(token, refreshToken ?: "")
        // refresh user profile and save current user id

        when (val refreshResult = refreshProfile()) {
            is Result.Success -> {
                dataStore.saveAuthUser(refreshResult.data.userId)
                refreshResult.data
            }

            is Result.Error -> {
                dataStore.deleteAuthToken()
                throw Exception("Could not refresh profile")
            }

        }
    }

    override suspend fun logout(): Result<Unit> = result {
        dataStore.deleteAuthToken()
    }

    override suspend fun profileExists(): Result<Boolean> = result {
        db.profileDao().exists()
    }

    override suspend fun getProfile(): Result<Profile?> = result {
        db.database().withTransaction {
            if (db.profileDao().exists()) {
                db.profileDao().getProfile().toDomain()
            } else null
        }
    }

    override fun getProfileFlow(): Flow<Result<Profile?>> {
        return db.profileDao().getProfileFlow().map {
            it?.toDomain()
        }.asResult()
    }

    override suspend fun updateProfile(profile: Profile): Result<Profile> = result {
        api.updateProfile(profile).successOrException {
            db.profileDao().update(it.toEntity())
            db.profileDao().getProfile()?.toDomain() ?: throw RuntimeException("Profile not found")
        }
    }

    override suspend fun getProfileStats(): Result<AthleteStats?> = result {
        api.profile().successOrException { it.stats }
    }

}

sealed class CheckLoginResult {
    data class LoggedIn(val profile: Profile) : CheckLoginResult()
    object NotLoggedIn : CheckLoginResult()
    object LoginExpired : CheckLoginResult()
}