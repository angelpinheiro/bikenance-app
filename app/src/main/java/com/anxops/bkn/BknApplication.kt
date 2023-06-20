package com.anxops.bkn

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.anxops.bkn.network.Api
import com.anxops.bkn.network.KtorClient
import com.anxops.bkn.storage.*
import com.anxops.bkn.storage.room.AppDb
import com.anxops.bkn.ui.Notifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidApp
class BknApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var notifier: Notifier

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()

    override fun onCreate() {
        super.onCreate()
        notifier.createNotificationChannel(this)
    }
}


@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context): BknDataStore =
        BknDataStore(context)

    @Provides
    @Singleton
    fun providesApi(dataStore: BknDataStore): Api = Api(KtorClient(), dataStore = dataStore)

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDb = AppDb(context)

    @Provides
    @Singleton
    fun providesProfileRepository(api: Api, db: AppDb): ProfileRepositoryFacade =
        ProfileRepository(api, db)

    @Provides
    @Singleton
    fun providesBikesRepository(
        api: Api,
        db: AppDb,
        rf: RidesRepositoryFacade
    ): BikeRepositoryFacade = BikeRepository(api, db, rf)

    @Provides
    @Singleton
    fun providesRidesRepository(api: Api, db: AppDb): RidesRepositoryFacade =
        BikeRidesRepository(api, db)

    @Provides
    @Singleton
    fun providesSynchronizer(
        pf: ProfileRepositoryFacade,
        bf: BikeRepositoryFacade,
        rf: RidesRepositoryFacade,
        db: AppDb
    ):
            DBSynchronizer = DBSynchronizer(db, pf, bf, rf)

    @Provides
    @Singleton
    fun providesNotifier(): Notifier = Notifier()

}