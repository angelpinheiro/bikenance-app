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

package com.anxops.bkn.ui.screens.rides.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.repository.RidesRepositoryFacade
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
    data class RideLoaded(val ride: BikeRide, val polyline: List<LatLng>?) : RideScreenState()
}

@HiltViewModel
class RideScreenViewModel @Inject constructor(
    val dataStore: BknDataStore,
    private val ridesRepository: RidesRepositoryFacade,
    private val bikeRepository: BikeRepositoryFacade,
) : ViewModel() {


    private val _state: MutableStateFlow<RideScreenState> = MutableStateFlow(RideScreenState.RideLoading)
    val state: StateFlow<RideScreenState> = _state

    val bikes = bikeRepository.getBikesFlow(false).stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    fun loadRide(rideId: String) {
//        if (state.value !is RideScreenState.RideLoaded) {
            viewModelScope.launch {
                val ride = ridesRepository.getRide(rideId)
                if (ride != null) {
                    _state.value = RideScreenState.RideLoaded(
                        ride,
                        ride.mapSummaryPolyline?.let { decodePoly(it) })
                } else {
                    _state.value = RideScreenState.RideNotFound
                }
            }
//        }
    }

    fun setRideBike(it: Bike) {
        viewModelScope.launch {
            when (val s = _state.value) {
                is RideScreenState.RideLoaded -> {

                    val updatedRide = s.ride.copy(
                        bikeId = it._id
                    )
//                    ridesRepository.updateRide(updatedRide)
                    _state.value = RideScreenState.RideLoaded(
                        ride = updatedRide,
                        polyline = s.polyline
                    )
                }

                else -> {}
            }
        }

    }

}
