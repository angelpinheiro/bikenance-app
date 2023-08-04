package com.anxops.bkn.ui.screens.bikeComponents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.Maintenance
import com.anxops.bkn.data.model.RevisionFrequency
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import com.anxops.bkn.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class EditingMaintenance(
    val original: Maintenance, val editingMaintenance: Maintenance = original.copy()
)

sealed interface BikeComponentScreenState {
    object Error : BikeComponentScreenState
    object Loading : BikeComponentScreenState
    data class Loaded(
        val maintenances: List<Maintenance>,
        val component: BikeComponent,
        val bike: Bike,
        val editingMaintenance: EditingMaintenance?
    ) : BikeComponentScreenState
}

@HiltViewModel
class BikeComponentScreenViewModel @Inject constructor(
    private val bikeRepository: BikeRepositoryFacade,
    private val ridesRepository: RidesRepositoryFacade
) : ViewModel() {


    private val isLoadingFlow = MutableStateFlow(false)
    private val bikeFlow = MutableStateFlow<Bike?>(null)
    private val componentFlow = MutableStateFlow<BikeComponent?>(null)
    private val editingMaintenanceFlow = MutableStateFlow<EditingMaintenance?>(null)

    val state: StateFlow<BikeComponentScreenState> = combine(
        isLoadingFlow, bikeFlow, componentFlow, editingMaintenanceFlow
    ) { loading, bike, component, editingMaintenance ->

        if (loading) {
            BikeComponentScreenState.Loading
        } else if (bike != null && component != null) {
            BikeComponentScreenState.Loaded(

                component = component,
                bike = bike,
                maintenances = component.maintenances ?: emptyList(),
                editingMaintenance = editingMaintenance
            )
        } else {
            BikeComponentScreenState.Error
        }
    }.stateIn(viewModelScope, WhileUiSubscribed, BikeComponentScreenState.Loading)

    fun loadComponent(bikeId: String, componentId: String) {
        viewModelScope.launch {
            // TODO: Handle errors
            val bike = bikeRepository.getBike(bikeId)!!
            val c = bikeRepository.getBikeComponent(componentId)!!

            bikeFlow.update { bike }
            componentFlow.update { c }
            isLoadingFlow.update { false }
        }
    }

    fun onMaintenanceFreqUpdate(frequency: RevisionFrequency) {

        editingMaintenanceFlow.update { em ->

            em?.let {
                val updated = it.editingMaintenance.copy(defaultFrequency = frequency)
                it.copy(editingMaintenance = updated)
            }

        }
    }

    fun onConfirmEdit() {
        viewModelScope.launch {
            val bike = bikeFlow.value ?: return@launch
            val component = componentFlow.value ?: return@launch
            isLoadingFlow.emit(true)
            editingMaintenanceFlow.update {
                it?.let {
                    bikeRepository.updateMaintenance(bike, it.editingMaintenance)
                    loadComponent(bike._id, component._id)
                }
                null
            }
            isLoadingFlow.emit(false)
        }
    }

    fun onComponentReplace() {
        viewModelScope.launch {
            val bike = bikeFlow.value ?: return@launch
            componentFlow.value?.let { bikeComponent ->
                bikeRepository.replaceComponent(bikeComponent)?.let {
                    loadComponent(bike._id, it._id)
                }

            }

        }

    }

    fun onMaintenanceWearUpdate(wear: Double) {
//        editingMaintenanceFlow.update {
//            it?.copy(editingMaintenance = it.editingMaintenance.copy(status = wear))
//        }
    }

    fun onMaintenanceEdit(m: Maintenance?) {
        editingMaintenanceFlow.update {
            m?.let { EditingMaintenance(it) }
        }
    }
}
