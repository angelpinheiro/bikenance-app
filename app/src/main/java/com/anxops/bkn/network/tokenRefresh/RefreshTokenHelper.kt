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

package com.anxops.bkn.network.tokenRefresh

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.anxops.bkn.BuildConfig
import com.anxops.bkn.network.Api
import com.anxops.bkn.network.ApiResponse
import com.anxops.bkn.storage.BknDataStore
import java.util.concurrent.TimeUnit

object RefreshTokenHelper {
    suspend fun performRefresh(
        dataStore: BknDataStore,
        api: Api
    ): Boolean {

        Log.d("RefreshTokenHelper", "Refreshing auth tokens...")
        dataStore.getRefreshToken()?.let {
            when (val result = api.updateRefreshToken(it)) {
                is ApiResponse.Success -> {
                    if (result.data.token != null && result.data.refreshToken != null) {
                        Log.d("RefreshTokenHelper", "Refresh token success!")
                        dataStore.saveAuthTokens(result.data.token, result.data.refreshToken)
                        return true
                    }
                }
                else -> {
                    // Logout user if we can not refresh token
                    dataStore.deleteAuthToken()
                }
            }
        }
        Log.d("RefreshTokenHelper", "Refresh token failure!")
        return false
    }

    fun startPeriodicRefreshTokenWork(context: Context) {
        val workConstraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicRefreshTokenWork = PeriodicWorkRequest.Builder(
            RefreshTokenWork::class.java,
            1,
            TimeUnit.DAYS
        ).setConstraints(workConstraints).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "${BuildConfig.APPLICATION_ID}:token-refresh-work",
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicRefreshTokenWork
        )


    }


}