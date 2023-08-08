package com.anxops.bkn.ui.screens.garage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import com.anxops.bkn.data.repository.successOrException
import com.anxops.bkn.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class GarageScreenState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val bikes: List<Bike> = emptyList(),
    val selectedBike: Bike? = null,
    val lastRides: List<BikeRide> = emptyList()
)

@HiltViewModel
class GarageViewModel @Inject constructor(
    private val profileRepository: ProfileRepositoryFacade,
    private val bikeRepository: BikeRepositoryFacade,
    private val ridesRepository: RidesRepositoryFacade
) : ViewModel() {

    private val isRefreshingFlow = MutableStateFlow(false)
    private val bikesFlow = bikeRepository.getBikesFlow(fillComponents = true)
    private val selectedBikeFlow = MutableStateFlow<Bike?>(null)

    val screenState: StateFlow<GarageScreenState> = combine(
        bikesFlow,
        selectedBikeFlow,
        isRefreshingFlow
    ) { bikesResult, selectedBikeResult, refreshResult ->

        val lastRides = selectedBikeResult?.let { bike ->
            ridesRepository.getLastRides(bike._id).successOrException { it }
        }

        GarageScreenState(
            isLoading = false,
            isRefreshing = refreshResult,
            bikes = bikesResult,
            selectedBike = selectedBikeResult,
            lastRides = lastRides ?: emptyList()
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
