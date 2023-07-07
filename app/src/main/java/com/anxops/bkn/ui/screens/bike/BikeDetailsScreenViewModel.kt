package com.anxops.bkn.ui.screens.bike

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.ComponentTypes
import com.anxops.bkn.data.model.MaintenanceConfigurations
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.ComponentRepositoryFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class BikeDetailsScreenScreenState(
    val bike: Bike? = null,
    val selectedComponentTypes: Set<ComponentTypes> = setOf()
)

@HiltViewModel
class BikeDetailsScreenViewModel @Inject constructor(
    private val bikeComponentRepository: ComponentRepositoryFacade,
    private val bikesRepository: BikeRepositoryFacade,
    val db: AppDb,
    val api: Api,

    ) : ViewModel() {

    private val _state = MutableStateFlow(BikeDetailsScreenScreenState())
    val state: StateFlow<BikeDetailsScreenScreenState> = _state


    val selectedComponentTypes = mutableStateOf<Set<ComponentTypes>>(emptySet())

    fun loadBike(bikeId: String) {
        viewModelScope.launch {
            bikesRepository.getBike(bikeId)?.let {
                _state.value = state.value.copy(bike = it)
            }
        }
    }

    fun onSelectConfiguration(c: MaintenanceConfigurations) {
        viewModelScope.launch {
            selectedComponentTypes.value = c.types
            _state.value = _state.value.copy(
                selectedComponentTypes = c.types
            )
        }
    }

    fun onComponentTypeSelectionChange(it: Set<ComponentTypes>) {
        viewModelScope.launch {
            selectedComponentTypes.value = it
            _state.value = _state.value.copy(
                selectedComponentTypes = it
            )
        }
    }

}
