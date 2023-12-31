package com.anxops.bkn

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.util.DebugLogger
import com.anxops.bkn.data.network.tokenRefresh.TokenRefresher
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.ui.Notifier
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltAndroidApp
class BknApplication : Application(), Configuration.Provider, ImageLoaderFactory {

    @Inject
    lateinit var dispatcher: CoroutineDispatcher

    @Inject
    lateinit var dataStore: BknDataStore

    @Inject
    lateinit var tokenRefresher: TokenRefresher

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var notifier: Notifier

    override fun onCreate() {
        super.onCreate()
        configureLogger()
        configureFirebase()
        configureNotifications()
        configureTokenRefresh()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setWorkerFactory(workerFactory).build()
    }

    private fun configureTokenRefresh() {
        tokenRefresher.startPeriodicRefreshTokenWork(this)
    }

    private fun configureNotifications() {
        notifier.createNotificationChannel(this)
    }

    private fun configureLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun configureFirebase() {
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            task.result?.let { token ->
                MainScope().launch {
                    withContext(dispatcher) {
                        Timber.v("Saving firebase token: $token")
                        dataStore.saveFirebaseToken(token)
                    }
                }
            }
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .logger(DebugLogger())
            .respectCacheHeaders(false)
            .diskCache {
                DiskCache.Builder()
                    .directory(this.cacheDir.resolve("img_cache"))
                    .build()
            }
            .build()
    }
}
