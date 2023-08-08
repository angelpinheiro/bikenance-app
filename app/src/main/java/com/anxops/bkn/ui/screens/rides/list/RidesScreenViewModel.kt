package com.anxops.bkn.ui.screens.rides.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.entities.BikeRideEntity
import com.anxops.bkn.data.mediator.RideRemoteMediator
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.repository.AppInfoRepositoryFacade
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import com.anxops.bkn.ui.screens.rides.list.components.RideAndBike
import com.anxops.bkn.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RidesScreenViewModel @Inject constructor(
    private val ridesRepository: RidesRepositoryFacade,
    private val bikesRepository: BikeRepositoryFacade,
    private val appInfoRepository: AppInfoRepositoryFacade,
    val db: AppDb,
    val api: Api,
) : ViewModel() {

    val lastUpdatedFlow = appInfoRepository.appInfoFlow()
    val paginatedRidesFlow = createPaginatedRidesFlow(db, api)

    val bikes = bikesRepository.getBikesFlow().stateIn(
        viewModelScope, WhileUiSubscribed, emptyList()
    )

    val openActivityEvent: MutableSharedFlow<String> = MutableSharedFlow()

    fun openActivity(id: String) {
        viewModelScope.launch {
            openActivityEvent.emit(
                id
            )
        }
    }

    fun onBikeRideConfirmed(ride: BikeRide, bike: Bike?) {
        viewModelScope.launch {
            ridesRepository.updateRide(ride.copy(bikeId = bike?._id, bikeConfirmed = true))
        }
    }


    private fun createPaginatedRidesFlow(
        db: AppDb,
        api: Api,
    ): Flow<PagingData<RideAndBike>> = createPager(db, api, appInfoRepository).flow.map { p ->
        p.map { rideEntity ->
            val bike = bikes.value.find { rideEntity.bikeId == it._id }
            RideAndBike(rideEntity.toDomain(), bike)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun createPager(
        db: AppDb, api: Api, appInfoRepository: AppInfoRepositoryFacade
    ): Pager<Int, BikeRideEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = 20, prefetchDistance = 20
            ), pagingSourceFactory = {
                db.bikeRideDao().pagingSource()
            }, remoteMediator = RideRemoteMediator(
                db, api, appInfoRepository
            )
        )
    }


}
