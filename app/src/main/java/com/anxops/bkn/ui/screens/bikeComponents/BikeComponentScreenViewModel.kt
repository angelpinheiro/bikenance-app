package com.anxops.bkn.ui.screens.bikeComponents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.Maintenance
import com.anxops.bkn.data.model.RevisionFrequency
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import com.anxops.bkn.data.repository.data
import com.anxops.bkn.data.repository.isNullOrError
import com.anxops.bkn.data.repository.onError
import com.anxops.bkn.data.repository.onSuccess
import com.anxops.bkn.data.repository.onSuccessNotNull
import com.anxops.bkn.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

data class EditingMaintenance(
    val original: Maintenance,
    val editingMaintenance: Maintenance = original.copy()
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
        isLoadingFlow,
        bikeFlow,
        componentFlow,
        editingMaintenanceFlow
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
            val bikeResult = bikeRepository.getBike(bikeId)
            val compResult = bikeRepository.getBikeComponent(componentId)
            if (bikeResult.isNullOrError() || compResult.isNullOrError()) {
                // TODO: handle bike or component not load
            } else {
                bikeFlow.update { bikeResult.data() }
                componentFlow.update { compResult.data() }
                isLoadingFlow.update { false }
            }
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
                    bikeRepository.updateMaintenance(bike, it.editingMaintenance).onSuccess {
                        Timber.d("Success")
                        loadComponent(bike._id, component._id)
                    }.onError {
                        Timber.d("Error")
                    }
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
                bikeRepository.replaceComponent(bikeComponent).onSuccessNotNull {
                    loadComponent(bike._id, it._id)
                }.onError {
                    // TODO: Handle error
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
