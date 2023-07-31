package com.anxops.bkn.ui.screens.garage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import com.anxops.bkn.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


data class GarageScreenState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val bikes: List<Bike> = emptyList(),
    val selectedBike: Bike? = null,
    val lastRides: List<BikeRide> = emptyList(),
)


@HiltViewModel
class GarageViewModel @Inject constructor(
    private val profileRepository: ProfileRepositoryFacade,
    private val bikeRepository: BikeRepositoryFacade,
    private val ridesRepository: RidesRepositoryFacade
) : ViewModel() {

    private val isRefreshingFlow = MutableStateFlow(false)
    private val bikesFlow = bikeRepository.getBikesFlow(full = true)
    private val selectedBikeFlow = MutableStateFlow<Bike?>(null)
    private val lastRides = selectedBikeFlow.map { bike ->
        bike?.let { ridesRepository.getLastBikeRides(it._id) } ?: emptyList()
    }

    val screenState: StateFlow<GarageScreenState> = combine(
        bikesFlow,
        selectedBikeFlow,
        lastRides,
        isRefreshingFlow
    ) { bikesResult, selectedBikeResult, lastRidesResult, refreshResult ->
        GarageScreenState(
            isLoading = false,
            isRefreshing = refreshResult,
            bikes = bikesResult,
            selectedBike = selectedBikeResult,
            lastRides = lastRidesResult,
        )
    }.stateIn(viewModelScope, WhileUiSubscribed, GarageScreenState(isLoading = true))

    fun loadData() {
        viewModelScope.launch {
            isRefreshingFlow.emit(true)
            bikeRepository.refreshBikes()
            isRefreshingFlow.emit(false)
        }
    }

    fun setSelectedBike(bike: Bike) {
        viewModelScope.launch {
            selectedBikeFlow.emit(bike)
        }
    }

}

