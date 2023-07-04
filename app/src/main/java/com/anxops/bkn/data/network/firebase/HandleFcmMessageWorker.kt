package com.anxops.bkn.data.network.firebase

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import com.anxops.bkn.ui.NotificationData
import com.anxops.bkn.ui.Notifier
import com.anxops.bkn.ui.screens.destinations.HomeScreenDestination
import com.anxops.bkn.ui.screens.destinations.RideScreenDestination
import com.anxops.bkn.ui.screens.home.HomeSections
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