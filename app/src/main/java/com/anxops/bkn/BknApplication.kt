package com.anxops.bkn

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.anxops.bkn.data.network.tokenRefresh.RefreshTokenHelper
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.ui.Notifier
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltAndroidApp
class BknApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var dispatcher: CoroutineDispatcher

    @Inject
    lateinit var dataStore: BknDataStore

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var notifier: Notifier

    override fun onCreate() {
        super.onCreate()
        configureFirebase()
        configureNotifications()
        configureTokenRefresh()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setWorkerFactory(workerFactory).build()
    }

    private fun configureTokenRefresh() {
        RefreshTokenHelper.startPeriodicRefreshTokenWork(this)
    }

    private fun configureNotifications() {
        notifier.createNotificationChannel(this)
    }

    private fun configureFirebase() {
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            task.result?.let { token ->
                MainScope().launch {
                    withContext(dispatcher) {
                        dataStore.saveFirebaseToken(token)
                    }
                }
            }
        }
    }
}


