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

package com.anxops.bkn.data.network.firebase

import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


enum class MessageType(
  val type: String
) {
    NEW_ACTIVITY("NEW_ACTIVITY"),
    RIDES_UPDATED("RIDES_UPDATED"),
    RIDES_DELETED("RIDES_DELETED")
}

@AndroidEntryPoint
class MessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var dataStore: BknDataStore

    @Inject
    lateinit var ridesRepository: RidesRepositoryFacade

    override fun onNewToken(token: String) {
        Log.d("MessagingService", "New token received $token")
        SendTokenToServerWorker.launch(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("MessagingService", "Message data payload: ${remoteMessage.data}")
            val work = OneTimeWorkRequest.Builder(HandleFcmMessageWorker::class.java)
                .setInputData(Data.Builder().putAll(remoteMessage.data as Map<String, Any>).build())
                .build()
            WorkManager.getInstance(this).beginWith(work).enqueue()



        }
    }

}