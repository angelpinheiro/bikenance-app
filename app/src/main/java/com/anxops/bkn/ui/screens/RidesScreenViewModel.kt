package com.anxops.bkn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.model.Bike
import com.anxops.bkn.model.BikeRide
import com.anxops.bkn.storage.BikeRepositoryFacade
import com.anxops.bkn.storage.RidesRepositoryFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


data class RidesScreenState(
    val refreshing: Boolean = false
)

@HiltViewModel
class RidesScreenViewModel @Inject constructor(
    private val ridesRepository: RidesRepositoryFacade,
    private val bikesRepository: BikeRepositoryFacade
) : ViewModel() {

    private var _state = MutableStateFlow(RidesScreenState())
    val state: StateFlow<RidesScreenState> = _state

    val rides: StateFlow<List<BikeRide>?> =
        ridesRepository.getRidesFlow().stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val bikes: StateFlow<List<Bike>> =
        bikesRepository.getBikesFlow().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val openActivityEvent: MutableSharedFlow<String> = MutableSharedFlow()

    fun openActivity(id: String) {
        viewModelScope.launch {
            openActivityEvent.emit(
                id
            )
        }
    }


}
