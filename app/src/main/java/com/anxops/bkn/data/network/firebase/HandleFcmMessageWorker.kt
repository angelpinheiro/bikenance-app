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

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import com.anxops.bkn.ui.NotificationData
import com.anxops.bkn.ui.Notifier
import com.anxops.bkn.ui.screens.home.HomeSections
import com.anxops.bkn.ui.screens.destinations.RideScreenDestination
import com.anxops.bkn.ui.screens.destinations.HomeScreenDestination
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class HandleFcmMessageWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val ridesRepository: RidesRepositoryFacade,
    private val notifier: Notifier
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        when (inputData.getString("app_message_type")) {

            MessageType.RIDES_UPDATED.type -> {
                ridesRepository.reloadData()

                val count = inputData.getString("count") ?: "0"
                notifier.show(
                    applicationContext, NotificationData.DestinationDeepLink(
                        title = "Rides updated!",
                        text = "$count rides where imported from strava!",
                        to = HomeScreenDestination.invoke(section = HomeSections.Rides.id)
                    )
                )
            }

            MessageType.RIDES_DELETED.type -> {
                ridesRepository.reloadData()
            }
            MessageType.NEW_ACTIVITY.type -> {
                ridesRepository.reloadData()

                val id = inputData.getString("id") ?: ""
                notifier.show(
                    applicationContext, NotificationData.DestinationDeepLink(
                        title = "New activity!",
                        text = "New activity imported from strava ${id?.let { "($id)" }}!",
                        to = RideScreenDestination.invoke(id)
                    )
                )

            }
        }

        return Result.success()
    }

    companion object {
        private const val TAG = "SendTokenToServerWorker"
    }
}