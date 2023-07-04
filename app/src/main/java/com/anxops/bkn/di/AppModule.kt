/*
 * Copyright 2023 Marco Cattaneo
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

package com.anxops.bkn.di

import android.content.Context
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.KtorClient
import com.anxops.bkn.data.repository.BikeRepository
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.BikeRidesRepository
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.DBSynchronizer
import com.anxops.bkn.data.repository.ProfileRepository
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.ui.Notifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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