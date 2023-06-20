package com.anxops.bkn.storage

import com.anxops.bkn.model.Profile
import com.anxops.bkn.network.Api
import com.anxops.bkn.network.ApiResponse
import com.anxops.bkn.storage.room.AppDb
import com.anxops.bkn.storage.room.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileRepository(val api: Api, val db: AppDb) : ProfileRepositoryFacade {

    override suspend fun reloadData(): Boolean {
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
        return success

    }

    override suspend fun getProfile(): Profile? {
        return db.profileDao().getProfile()?.toDomain()
    }

    override fun getProfileFlow(): Flow<Profile?> {
        return db.profileDao().getProfileFlow().map {
            it?.toDomain()
        }
    }

    override suspend fun updateProfile(profile: Profile) {
        db.profileDao().update(profile.toEntity())
        api.updateProfile(profile)
    }

}