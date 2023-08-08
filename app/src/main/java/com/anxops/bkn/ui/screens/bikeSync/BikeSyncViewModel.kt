package com.anxops.bkn.ui.screens.bikeSync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class BikeSyncScreenStatus {
    LOADING, LOADED, SAVING, DONE
}

data class BikeSyncState(
    val status: BikeSyncScreenStatus,
    val bikes: Map<Bike, Boolean> = emptyMap()
)

@HiltViewModel
class BikeSyncViewModel @Inject constructor(
    private val bikeRepository: BikeRepositoryFacade
) : ViewModel() {

    private val statusFlow = MutableStateFlow(BikeSyncScreenStatus.LOADING)
    private val syncBikesFlow = MutableStateFlow<Map<Bike, Boolean>>(emptyMap())

    val state: StateFlow<BikeSyncState> = combine(statusFlow, syncBikesFlow) { status, syncBikes ->
        BikeSyncState(status = status, bikes = syncBikes)
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = BikeSyncState(status = BikeSyncScreenStatus.LOADING)
    )

    init {
        loadBikes()
    }

    private fun loadBikes() {
        viewModelScope.launch {
            bikeRepository.getBikes().let { bikes ->
                syncBikesFlow.update {
                    bikes.associateWith { !it.draft }
                }
                statusFlow.emit(BikeSyncScreenStatus.LOADED)
            }
        }
    }

    fun syncBike(bike: Bike, sync: Boolean) {
        viewModelScope.launch {
            syncBikesFlow.update {
                it.plus(bike to sync)
            }
        }
    }

    fun performSync() {
        viewModelScope.launch {
            statusFlow.emit(BikeSyncScreenStatus.SAVING)
            val idToSync = syncBikesFlow.value.map { it.key._id to it.value }.toMap()
            bikeRepository.updateSynchronizedBikes(idToSync)
            statusFlow.emit(BikeSyncScreenStatus.DONE)
        }
    }
}
