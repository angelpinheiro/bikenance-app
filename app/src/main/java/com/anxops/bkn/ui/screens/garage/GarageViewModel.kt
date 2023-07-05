package com.anxops.bkn.ui.screens.garage

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeScreenState(
    val refreshing: Boolean = false
)

@HiltViewModel
class GarageViewModel @Inject constructor(
    private val db: AppDb,
    private val api: Api,
    private val bikeRepository: BikeRepositoryFacade,
    private val ridesRepository: RidesRepositoryFacade
) :
    ViewModel() {

    private var _state = MutableStateFlow(HomeScreenState())

    val state: StateFlow<HomeScreenState> = _state

    val bikes: StateFlow<List<Bike>> = bikeRepository.getBikesFlow().map { bikes ->
        bikes.sortedByDescending { it.distance }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val selectedBike: MutableStateFlow<Bike?> = MutableStateFlow(null)

    val selectedBikeRides = mutableStateOf<List<BikeRide>>(emptyList())


    fun reload() {
        viewModelScope.launch {
            _state.value = state.value.copy(refreshing = true)
            bikeRepository.reloadData()
            _state.value = state.value.copy(refreshing = false)
        }
    }

    fun setSelectedBike(bike: Bike) {
        viewModelScope.launch {
            val rides = ridesRepository.getLastBikeRides(bike._id)
            selectedBikeRides.value = rides
            selectedBike.value = bike
        }

    }

}
