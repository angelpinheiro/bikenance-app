package com.anxops.bkn.ui.screens.garage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import com.anxops.bkn.util.mutableStateIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeScreenState(
    val refreshing: Boolean = false
)

sealed class GarageScreenState {
    object Loading : GarageScreenState()
    data class ShowingGarage(
        val bikes: List<Bike>,
        val selectedBike: Bike?,
        val lastRides: List<BikeRide>?,
    ) : GarageScreenState()
}


@HiltViewModel
class GarageViewModel @Inject constructor(
    private val profileRepository: ProfileRepositoryFacade,
    private val bikeRepository: BikeRepositoryFacade,
    private val ridesRepository: RidesRepositoryFacade
) : ViewModel() {

    private val _screenState: MutableStateFlow<GarageScreenState> =
        bikeRepository.getBikesFlow(draft = false, full = true).map { bikes ->
            val selectedBike = bikes.firstOrNull()
            val rides = selectedBike?.let { ridesRepository.getLastBikeRides(it._id) }
            GarageScreenState.ShowingGarage(
                bikes, selectedBike, rides
            )
        }.mutableStateIn(viewModelScope, GarageScreenState.Loading)

    val screenState: StateFlow<GarageScreenState> = _screenState

    fun loadData() {
        viewModelScope.launch {
            _screenState.value = GarageScreenState.Loading
            bikeRepository.refreshBikes()
        }
    }

    fun setSelectedBike(bike: Bike) {
        viewModelScope.launch {
            _screenState.value.let { currentState ->
                if (currentState is GarageScreenState.ShowingGarage) {
                    _screenState.value = currentState.copy(
                        selectedBike = bike, lastRides = ridesRepository.getLastBikeRides(bike._id)
                    )
                }
            }
        }
    }

}

