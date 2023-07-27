package com.anxops.bkn.ui.screens.rides.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import com.anxops.bkn.ui.screens.rides.list.components.RideAndBike
import com.anxops.bkn.util.decodePoly
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RideScreenState {
    object RideLoading : RideScreenState()
    object RideNotFound : RideScreenState()
    data class RideLoaded(val item: RideAndBike, val polyline: List<LatLng>?) : RideScreenState()
}

@HiltViewModel
class RideScreenViewModel @Inject constructor(
    val dataStore: BknDataStore,
    private val ridesRepository: RidesRepositoryFacade,
    private val bikeRepository: BikeRepositoryFacade,
) : ViewModel() {


    private val _state: MutableStateFlow<RideScreenState> =
        MutableStateFlow(RideScreenState.RideLoading)
    val state: StateFlow<RideScreenState> = _state

    val bikes =
        bikeRepository.getBikesFlow(false).stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    fun loadRide(rideId: String) {

        viewModelScope.launch {
            val ride = ridesRepository.getRide(rideId)
            if (ride != null) {
                val bike = ride.bikeId?.let { bikeRepository.getBike(it) }
                _state.value = RideScreenState.RideLoaded(
                    RideAndBike(ride, bike),
                    ride.mapSummaryPolyline?.let { decodePoly(it) })
            } else {
                _state.value = RideScreenState.RideNotFound
            }
        }
    }

    fun setRideBike(bike: Bike) {
        viewModelScope.launch {
            when (val s = _state.value) {
                is RideScreenState.RideLoaded -> {
                    val updatedRide = s.item.copy(
                        ride = s.item.ride.copy(bikeId = bike._id, bikeConfirmed = true)
                    )
                    ridesRepository.updateRide(updatedRide.ride)
                    _state.value = RideScreenState.RideLoaded(
                        item = updatedRide, polyline = s.polyline
                    )
                }

                else -> {}
            }
        }

    }

}
