package com.anxops.bkn.data.network.firebase

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.preferences.BknDataStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

class SendTokenToServerWorkerStarter(val context: Context) {
    fun start() {
        SendTokenToServerWorker.launch(context)
    }
}

@HiltWorker
class SendTokenToServerWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val dataStore: BknDataStore,
    val api: Api
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        dataStore.getAuthToken()?.let {
            dataStore.getFirebaseToken()?.let {
                api.updateFirebaseToken(it)
                Timber.d("Sending firebase token to server success")
            }
        }
        return Result.success()
    }

    companion object {
        fun launch(context: Context) {
            val work = OneTimeWorkRequest.Builder(SendTokenToServerWorker::class.java).build()
            WorkManager.getInstance(context).beginWith(work).enqueue()
        }
    }
}
