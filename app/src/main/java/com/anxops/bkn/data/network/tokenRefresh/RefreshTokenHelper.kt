package com.anxops.bkn.data.network.tokenRefresh

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.anxops.bkn.BuildConfig
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.ApiResponse
import com.anxops.bkn.data.preferences.BknDataStore
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

                is ApiResponse.Error -> {
                    // TODO: Logout user if we can not refresh token?
                    // dataStore.deleteAuthToken()
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