package com.anxops.bkn.di

import android.content.Context
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.ConnectivityChecker
import com.anxops.bkn.data.network.ImageUploader
import com.anxops.bkn.data.network.KtorClient
import com.anxops.bkn.data.network.firebase.FirebaseImageUploader
import com.anxops.bkn.data.network.firebase.SendTokenToServerWorkerStarter
import com.anxops.bkn.data.network.tokenRefresh.DefaultTokenRefresher
import com.anxops.bkn.data.network.tokenRefresh.TokenRefresher
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.repository.AppInfoRepository
import com.anxops.bkn.data.repository.AppInfoRepositoryFacade
import com.anxops.bkn.data.repository.BikeRepository
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.BikeRidesRepository
import com.anxops.bkn.data.repository.ProfileRepository
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import com.anxops.bkn.ui.Notifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun providesIODispatchers(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context): BknDataStore = BknDataStore(context)

    @Provides
    @Singleton
    fun providesWorkerStarter(@ApplicationContext context: Context): SendTokenToServerWorkerStarter =
        SendTokenToServerWorkerStarter(context)

    @Provides
    @Singleton
    fun providesTokenRefresher(dataStore: BknDataStore, api: Api): TokenRefresher = DefaultTokenRefresher(dataStore, api)

    @Provides
    @Singleton
    fun providesApi(dataStore: BknDataStore): Api = Api(KtorClient(), dataStore = dataStore)

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDb = AppDb(context)

    @Provides
    @Singleton
    fun providesProfileRepository(
        api: Api,
        db: AppDb,
        dataStore: BknDataStore
    ): ProfileRepositoryFacade = ProfileRepository(api, db, dataStore)

    @Provides
    @Singleton
    fun providesBikesRepository(
        api: Api,
        db: AppDb,
        rf: RidesRepositoryFacade
    ): BikeRepositoryFacade = BikeRepository(api, db, rf)

    @Provides
    @Singleton
    fun providesRidesRepository(api: Api, db: AppDb): RidesRepositoryFacade = BikeRidesRepository(api, db)

    @Provides
    @Singleton
    fun providesAppInfoRepository(api: Api, db: AppDb): AppInfoRepositoryFacade = AppInfoRepository(api, db)

    @Provides
    @Singleton
    fun providesNotifier(): Notifier = Notifier()

    @Provides
    @Singleton
    fun providesImageUploader(): ImageUploader = FirebaseImageUploader()

    @Provides
    @Singleton
    fun providesConnectivityChecker(@ApplicationContext context: Context): ConnectivityChecker = ConnectivityChecker(context)
}
