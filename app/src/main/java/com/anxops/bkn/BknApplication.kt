package com.anxops.bkn

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.anxops.bkn.network.tokenRefresh.RefreshTokenHelper
import com.anxops.bkn.storage.DBSynchronizer
import com.anxops.bkn.ui.Notifier
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BknApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var dbSynchronizer: DBSynchronizer

    @Inject
    lateinit var notifier: Notifier

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()

    override fun onCreate() {
        super.onCreate()
        RefreshTokenHelper.startPeriodicRefreshTokenWork(this)
    }
}


