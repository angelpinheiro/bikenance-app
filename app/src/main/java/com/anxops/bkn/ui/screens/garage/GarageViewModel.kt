package com.anxops.bkn.ui.screens.garage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import com.anxops.bkn.util.mutableStateIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeScreenState(
    val refreshing: Boolean = false
)


sealed class GarageScreenState {
    object Loading : GarageScreenState()
    data class ShowingGarage(
        val bikes: List<Bike>,
        val allBikes: List<Bike>,
        val selectedBike: Bike?,
        val lastRides: List<BikeRide>?,
        val showSync: Boolean = false
    ) : GarageScreenState()

//    data class ShowingBikeSync(val allBikes: List<Bike>) : GarageScreenState()

}


@HiltViewModel
class GarageViewModel @Inject constructor(
    private val bikeRepository: BikeRepositoryFacade,
    private val ridesRepository: RidesRepositoryFacade
) : ViewModel() {

    private val _screenState: MutableStateFlow<GarageScreenState> =
        bikeRepository.getBikesFlow(true).map { allBikes ->

            val mustShowSync = allBikes.isNotEmpty() && allBikes.all { it.draft }
            val bikes = allBikes.filter { !it.draft }.sortedByDescending { it.distance }
            val selectedBike = bikes.firstOrNull()
            val rides = selectedBike?.let { ridesRepository.getLastBikeRides(it._id) }

            Log.d("GarageViewModel", "Collected ${allBikes.size} bikes (${bikes.size} sync) (rides: ${rides?.size ?: "--"})")

            GarageScreenState.ShowingGarage(
                bikes, allBikes, selectedBike, rides, mustShowSync
            )


        }.mutableStateIn(viewModelScope, GarageScreenState.Loading)

    val screenState: StateFlow<GarageScreenState> = _screenState

    fun loadData() {
        viewModelScope.launch {

            _screenState.value = GarageScreenState.Loading
            bikeRepository.reloadData()
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

    fun syncBike(bike: Bike, sync: Boolean) {
        viewModelScope.launch {
            _screenState.value.let { currentState ->
                if (currentState is GarageScreenState.ShowingGarage) {
                    _screenState.update {
                        currentState.copy(allBikes = currentState.allBikes.map {
                            if (it._id == bike._id) {
                                it.copy(draft = !sync)
                            } else {
                                it
                            }
                        })
                    }
                }
            }
        }
    }

    fun finishBikeSync() {
        viewModelScope.launch {
            viewModelScope.launch {
                _screenState.value.let { currentState ->
                    if (currentState is GarageScreenState.ShowingGarage) {
                        _screenState.value = GarageScreenState.Loading
                        bikeRepository.updateSynchronizedBikes(currentState.allBikes.associate { it._id to !it.draft })
                        bikeRepository.reloadData()
                    }
                }
            }
        }

    }

    fun onShowOrHideSync(show: Boolean) {
        viewModelScope.launch {
            _screenState.value.let { currentState ->
                if (currentState is GarageScreenState.ShowingGarage) {
                    _screenState.update {
                        currentState.copy(showSync = show)
                    }
                }
            }
        }

    }
}

