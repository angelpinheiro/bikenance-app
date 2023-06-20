package com.anxops.bkn.network.firebase

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.anxops.bkn.network.Api
import com.anxops.bkn.storage.BknDataStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SendTokenToServerWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val dataStore: BknDataStore,
    val api: Api
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): ListenableWorker.Result {
        dataStore.getAuthToken()?.let {
            dataStore.getFirebaseToken()?.let {
                Log.d(TAG, "Sending token to server: $it")
                api.updateFirebaseToken(it)
            }
        }
        return Result.success()
    }

    companion object {
        private const val TAG = "SendTokenToServerWorker"

        fun launch(context: Context) {
            val work = OneTimeWorkRequest.Builder(SendTokenToServerWorker::class.java).build()
            WorkManager.getInstance(context).beginWith(work).enqueue()
        }
    }
}