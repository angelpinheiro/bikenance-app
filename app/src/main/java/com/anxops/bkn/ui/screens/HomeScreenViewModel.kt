package com.anxops.bkn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.model.Bike
import com.anxops.bkn.storage.BikeRepositoryFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeScreenState(
    val refreshing: Boolean = false
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val bikeRepository: BikeRepositoryFacade
) :
    ViewModel() {

    private var _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _state

    val bikes: StateFlow<List<Bike>> = bikeRepository.getBikesFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun reload() {
        viewModelScope.launch {
            _state.value = state.value.copy(refreshing = true)
            bikeRepository.reloadData()
            _state.value = state.value.copy(refreshing = false)
        }
    }

}
