/*
 * Copyright 2023 Angel Piñeiro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anxops.bkn.data.repository

import com.anxops.bkn.data.model.Profile
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.ApiResponse
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.toEntity
import com.anxops.bkn.util.RepositoryResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


interface ProfileRepositoryFacade {

    suspend fun reloadData(): Boolean

    suspend fun getProfile(): Profile?

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