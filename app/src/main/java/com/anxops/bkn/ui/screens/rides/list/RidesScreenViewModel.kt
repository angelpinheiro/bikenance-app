package com.anxops.bkn.ui.screens.rides.list

import android.util.Log
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
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RidesScreenViewModel @Inject constructor(
    private val ridesRepository: RidesRepositoryFacade,
    private val bikesRepository: BikeRepositoryFacade,
    val db: AppDb,
    val api: Api,
) : ViewModel() {

    init {
        Log.d("RidesScreenViewModel", "RidesScreenViewModel Init")
    }

    val lastUpdatedFlow = db.appInfoDao().getAppInfoFlow()
    val paginatedRidesFlow = createPaginatedRidesFlow(db, api)

    val bikes: StateFlow<List<Bike>> =
        bikesRepository.getBikesFlow().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val openActivityEvent: MutableSharedFlow<String> = MutableSharedFlow()

    fun openActivity(id: String) {
        viewModelScope.launch {
            openActivityEvent.emit(
                id
            )
        }
    }


    private fun createPaginatedRidesFlow(
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


}