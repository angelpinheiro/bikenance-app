package com.anxops.bkn.ui.screens.bike

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeStatus
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class BikeScreenEvent {
    data class LoadBike(val bikeId: String) : BikeScreenEvent()
}

sealed class BikeScreenState {
    object Loading : BikeScreenState()
    data class Loaded(
        val bike: Bike
    ) : BikeScreenState()
}

@HiltViewModel
class BikeScreenViewModel @Inject constructor(
    private val bikesRepository: BikeRepositoryFacade,
) : ViewModel() {

    private val stateFlow = MutableStateFlow<BikeScreenState>(BikeScreenState.Loading)
    val state: StateFlow<BikeScreenState> = stateFlow

    fun handleEvent(event: BikeScreenEvent) {
        viewModelScope.launch {
            val newState = when (event) {
                is BikeScreenEvent.LoadBike -> {
                    loadBike(event.bikeId)
                }
            }
            newState?.let { stateFlow.value = it  }
        }
    }

    private suspend fun loadBike(bikeId: String): BikeScreenState? {
        return bikesRepository.getBike(bikeId)?.let { bike ->
            BikeScreenState.Loaded(bike = bike)
        }
    }

}
