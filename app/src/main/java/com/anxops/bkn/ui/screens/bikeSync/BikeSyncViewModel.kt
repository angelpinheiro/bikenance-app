package com.anxops.bkn.ui.screens.bikeSync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


enum class BikeSyncScreenStatus {
    LOADING, LOADED, SAVING, DONE
}

data class BikeSyncState(
    val status: BikeSyncScreenStatus, val bikes: Map<Bike, Boolean>
)


@HiltViewModel
class BikeSyncViewModel @Inject constructor(
    private val bikeRepository: BikeRepositoryFacade,
) : ViewModel() {


    private val _state = MutableStateFlow(BikeSyncState(BikeSyncScreenStatus.LOADING, emptyMap()))
    val state: StateFlow<BikeSyncState> = _state

    init {
        loadBikes()
    }

    private fun loadBikes() {
        viewModelScope.launch {
            bikeRepository.getBikes().let { bikes ->
                _state.value =
                    BikeSyncState(BikeSyncScreenStatus.LOADED, bikes.associateWith { !it.draft })
            }
        }
    }

    fun syncBike(bike: Bike, sync: Boolean) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                bikes = _state.value.bikes.plus(bike to sync)
            )
        }
    }


    fun performSync() {
        viewModelScope.launch {
            _state.value = _state.value.copy(status = BikeSyncScreenStatus.SAVING)
            val idToSync = state.value.bikes.map { it.key._id to it.value }.toMap()
            bikeRepository.updateSynchronizedBikes(idToSync)
            _state.value = _state.value.copy(status = BikeSyncScreenStatus.DONE)
        }
    }

}

