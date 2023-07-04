package com.anxops.bkn.data.network.tokenRefresh

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.preferences.BknDataStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class RefreshTokenWork @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val dataStore: BknDataStore,
    val api: Api
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return if (RefreshTokenHelper.performRefresh(dataStore, api))
            Result.success()
        else
            Result.failure()
    }
}