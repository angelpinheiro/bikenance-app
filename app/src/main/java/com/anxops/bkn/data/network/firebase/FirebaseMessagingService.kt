package com.anxops.bkn.data.network.firebase

import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import timber.log.Timber

enum class MessageType(
    val type: String
) {
    NEW_ACTIVITY("NEW_ACTIVITY"), RIDES_UPDATED("RIDES_UPDATED"), RIDES_DELETED("RIDES_DELETED"), PROFILE_SYNC("PROFILE_SYNC")
}

@AndroidEntryPoint
class MessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var dataStore: BknDataStore

    @Inject
    lateinit var ridesRepository: RidesRepositoryFacade

    override fun onNewToken(token: String) {
        Timber.d("New firebase token received. Starting SendTokenToServerWorker job...")
        SendTokenToServerWorker.launch(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            Timber.d("New push message received ${remoteMessage.data} ")
            val work = OneTimeWorkRequest.Builder(HandleFcmMessageWorker::class.java)
                .setInputData(Data.Builder().putAll(remoteMessage.data as Map<String, Any>).build()).build()
            WorkManager.getInstance(this).beginWith(work).enqueue()
        }
    }
}
