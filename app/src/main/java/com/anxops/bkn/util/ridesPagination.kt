package com.anxops.bkn.util

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.entities.BikeRideEntity
import com.anxops.bkn.data.mediator.RideRemoteMediator
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.network.Api
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
