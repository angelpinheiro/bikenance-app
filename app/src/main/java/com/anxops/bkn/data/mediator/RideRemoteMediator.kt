package com.anxops.bkn.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.entities.BikeRideEntity
import com.anxops.bkn.data.database.toEntity
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.ApiException
import com.anxops.bkn.data.network.ApiResponse
import com.anxops.bkn.data.repository.AppInfoRepositoryFacade
import java.io.IOException
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalPagingApi::class)
class RideRemoteMediator(
    private val db: AppDb,
    private val api: Api,
    private val appInfoRepository: AppInfoRepositoryFacade
) : RemoteMediator<Int, BikeRideEntity>() {
    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, BikeRideEntity>
    ): MediatorResult {
        try {
            val dateTime = when (loadType) {
                LoadType.REFRESH -> null
                // when appending, return the last item dateTime
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                    lastItem.dateTime
                }
                // we don't allow prepending data
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = false
                )
            }
            // query our api for items before that datetime (recent rides come first)
            val response = api.getPaginatedRidesByDateTime(
                dateTime, state.config.pageSize
            )
            return handleApiResponse(response, loadType, dateTime)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun handleApiResponse(
        response: ApiResponse<List<BikeRide>>, loadType: LoadType, dateTime: String?
    ): MediatorResult {
        return when (response) {
            is ApiResponse.Success -> {
                if (response.data.isEmpty()) {
                    appInfoRepository.saveLastRidesUpdate(System.currentTimeMillis())
                    MediatorResult.Success(true)
                } else {
                    db.database().withTransaction {
                        // it we are refreshing, clear rides before inserting the new ones
                        if (loadType == LoadType.REFRESH) {
                            db.bikeRideDao().clear()
                            appInfoRepository.saveLastRidesUpdate(System.currentTimeMillis())
                        }
                        response.data.forEach {
                            db.bikeRideDao().insert(it.toEntity())
                        }
                    }
                    MediatorResult.Success(false)
                }
            }

            is ApiResponse.Error -> {
                MediatorResult.Error(
                    ApiException.Unknown("Api response error -> LoadKey: [$dateTime]")
                )
            }
        }
    }

    // when the mediator is initialized, skip refresh if the last refresh was in the last hour.
    // This avoids continuous refreshes when jumping from screens or if the app is reopen
    override suspend fun initialize(): InitializeAction {

        val lastUpdate = db.appInfoDao().getAppInfo()?.lastRidesUpdate ?: 0
        val cacheTimeout = TimeUnit.HOURS.toMillis(1) // 1 hour

        return if (System.currentTimeMillis() - lastUpdate > cacheTimeout) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }


}
