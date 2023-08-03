package com.anxops.bkn.ui.screens.rides.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import com.anxops.bkn.data.repository.onSuccess
import com.anxops.bkn.data.repository.onSuccessNotNull
import com.anxops.bkn.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class RideScreenState(
    val isLoading: Boolean = true,
    val bike: Bike? = null,
    val allBikes: List<Bike> = emptyList(),
    val ride: BikeRide? = null
)

@HiltViewModel
class RideScreenViewModel @Inject constructor(
    val dataStore: BknDataStore,
    private val ridesRepository: RidesRepositoryFacade,
    private val bikeRepository: BikeRepositoryFacade,
) : ViewModel() {

    private val rideFlow = MutableStateFlow<BikeRide?>(null)
    private val loadingFLow = MutableStateFlow<Boolean>(true)
    private val allBikes =
        bikeRepository.getBikesFlow(false).stateIn(viewModelScope, WhileUiSubscribed, emptyList())

    val state: StateFlow<RideScreenState> =
        combine(rideFlow, loadingFLow, allBikes) { ride, loading, allBikes ->
            val bike = allBikes.find { it._id == ride?.bikeId }
            RideScreenState(loading, bike, allBikes, ride)
        }.stateIn(viewModelScope, WhileUiSubscribed, RideScreenState(isLoading = true))


    fun loadRide(rideId: String) {
        viewModelScope.launch {
            loadingFLow.emit(true)
            ridesRepository.getRide(rideId).onSuccessNotNull { ride ->
                rideFlow.update { ride }
            }
            loadingFLow.emit(false)
        }
    }

    fun setRideBike(bike: Bike) {
        viewModelScope.launch {
            rideFlow.value?.let { ride ->
                loadingFLow.emit(true)
                ridesRepository.updateRide(ride.copy(bikeId = bike._id, bikeConfirmed = true))
                    .onSuccess {
                        rideFlow.update { it }
                    }
                loadingFLow.emit(false)
            }
        }

    }

}
