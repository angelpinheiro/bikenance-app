package com.anxops.bkn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.anxops.bkn.model.Bike
import com.anxops.bkn.model.BikeRide
import com.anxops.bkn.network.Api
import com.anxops.bkn.storage.BikeRepositoryFacade
import com.anxops.bkn.storage.RideRemoteMediator
import com.anxops.bkn.storage.RidesRepositoryFacade
import com.anxops.bkn.storage.room.AppDb
import com.anxops.bkn.storage.room.entities.BikeRideEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


data class RidesScreenState(
    val refreshing: Boolean = false
)

@HiltViewModel
class RidesScreenViewModel @Inject constructor(
    private val ridesRepository: RidesRepositoryFacade,
    private val bikesRepository: BikeRepositoryFacade,
    val db: AppDb,
    val api: Api,
) : ViewModel() {

    init {
        viewModelScope.launch {
//            db.bikeRideDao().clear()
        }
    }

    fun getRidesFlow() = createPaginatedRidesFlow(db, api)

    private var _state = MutableStateFlow(RidesScreenState())
    val state: StateFlow<RidesScreenState> = _state

    val rides: StateFlow<List<BikeRide>?> =
        ridesRepository.getRidesFlow().stateIn(viewModelScope, SharingStarted.Eagerly, null)

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

    private fun createPaginatedRidesFlow(
        db: AppDb,
        api: Api,
    ): Flow<PagingData<BikeRide>> = createPager(db, api).flow.map { p ->
        p.map {
            it.toDomain()
        }
    }


}
