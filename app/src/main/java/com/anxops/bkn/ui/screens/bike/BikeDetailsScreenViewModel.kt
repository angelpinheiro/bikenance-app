package com.anxops.bkn.ui.screens.bike

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeStatus
import com.anxops.bkn.data.model.BikeType
import com.anxops.bkn.data.model.ComponentTypes
import com.anxops.bkn.data.model.getDefaultComponents
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


data class BikeWithStatus(
    val bike: Bike, val status: BikeStatus
)

@HiltViewModel
class BikeDetailsScreenViewModel @Inject constructor(
    private val bikesRepository: BikeRepositoryFacade,
    val db: AppDb,
    val api: Api,

    ) : ViewModel() {


    val selectedComponentTypes = mutableStateOf<Set<ComponentTypes>>(emptySet())

    private val selectedBikeId = MutableStateFlow<String?>(null)

    private val bikeFlow = selectedBikeId.mapLatest {
        it?.let { id ->
            bikesRepository.getBike(id)?.let { bike -> BikeWithStatus(bike, bike.status()) }
        } ?: null
    }

    val bike = bikeFlow.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun loadBike(bikeId: String) {
        viewModelScope.launch {
            selectedBikeId.value = bikeId
        }
    }

    fun onSelectConfiguration(t: BikeType) {
        viewModelScope.launch {
            selectedComponentTypes.value = getDefaultComponents(t)
        }
    }

    fun onComponentTypeSelectionChange(it: Set<ComponentTypes>) {
        viewModelScope.launch {
            selectedComponentTypes.value = it
        }
    }
}
