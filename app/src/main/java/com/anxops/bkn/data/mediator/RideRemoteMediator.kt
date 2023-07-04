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

package com.anxops.bkn.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.ApiResponse
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.entities.AppInfo
import com.anxops.bkn.data.database.entities.BikeRideEntity
import com.anxops.bkn.data.database.toEntity
import java.io.IOException
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalPagingApi::class)
class RideRemoteMediator(
    private val db: AppDb,
    private val api: Api,
) : RemoteMediator<Int, BikeRideEntity>() {
    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, BikeRideEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = false
                )

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                    lastItem.dateTime
                }
            }

            val response = api.getPaginatedRidesByDateTime(
                loadKey, state.config.pageSize
            )

            when (response) {
                is ApiResponse.Success -> {

                    Log.d(
                        "RideRemoteMediator",
                        "Success -> LoadType: [$loadType], LoadKey: [$loadKey], [${response.data.size} items loaded]"
                    )
                    if (response.data.isEmpty()) {
                        return MediatorResult.Success(true)
                    }
                    db.database().run {
                        if (loadType == LoadType.REFRESH) {
                            db.bikeRideDao().clear()
                            db.appInfoDao().clear()
                            db.appInfoDao()
                                .insert(AppInfo(lastRidesUpdate = System.currentTimeMillis()))
                        }
                        response.data.forEach {
                            db.bikeRideDao().insert(it.toEntity())
                        }
                    }

                    return MediatorResult.Success(false)
                }

                is ApiResponse.Error -> {

                    Log.d(
                        "RideRemoteMediator", "Error -> LoadKey: [$loadKey] [${response.message}]"
                    )

                    return MediatorResult.Error(
                        Exception("ApiResponse error")
                    )
                }
            }


        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val lastUpdate = db.appInfoDao().getAppInfo()?.lastRidesUpdate ?: 0
        val cacheTimeout = TimeUnit.HOURS.toMillis(1) // 1 hour

        return if (System.currentTimeMillis() - lastUpdate > cacheTimeout) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }


}
