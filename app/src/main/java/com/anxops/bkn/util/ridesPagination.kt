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

package com.anxops.bkn.util

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.mediator.RideRemoteMediator
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.entities.BikeRideEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun createPaginatedRidesFlow(
    db: AppDb,
    api: Api,
): Flow<PagingData<BikeRide>> = createPager(db, api).flow.map { p ->
    p.map {
        it.toDomain()
    }
}

@OptIn(ExperimentalPagingApi::class)
fun createPager(
    db: AppDb,
    api: Api,
): Pager<Int, BikeRideEntity> {
    return Pager(
        config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 20
        ),
        pagingSourceFactory = {
            db.bikeRideDao().pagingSource()
        },
        remoteMediator = RideRemoteMediator(
            db, api
        )
    )
}
