package com.anxops.bkn.ui.screens.maintenances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.Maintenance
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
import timber.log.Timber
import javax.inject.Inject


data class MaintenanceRef(
    val bike: Bike, val component: BikeComponent, val maintenance: Maintenance
)

data class MaintenanceScreenState(
    val loading: Boolean, val maintenance: MaintenanceRef? = null
)

@HiltViewModel
class MaintenanceScreenViewModel @Inject constructor(
    private val bikeRepository: BikeRepositoryFacade,
    private val ridesRepository: RidesRepositoryFacade
) : ViewModel() {


    private val isLoadingFlow = MutableStateFlow(false)
    private val maintenanceFlow = MutableStateFlow<MaintenanceRef?>(null)

    val state: StateFlow<MaintenanceScreenState> =
        combine(isLoadingFlow, maintenanceFlow) { loading, bm ->
            MaintenanceScreenState(loading, bm)
        }.stateIn(viewModelScope, WhileUiSubscribed, MaintenanceScreenState(loading = true))

    fun loadMaintenance(bikeId: String, maintenanceId: String) {

        Timber.d("Load maintenance $bikeId $maintenanceId")

        viewModelScope.launch {

            // TODO: Handle errors
            val bike = bikeRepository.getBike(bikeId)!!
            val m = bikeRepository.getBikeMaintenance(maintenanceId)
            val c = m?.let { bikeRepository.getBikeComponent(it.componentId) }!!

            maintenanceFlow.update {
                MaintenanceRef(bike, c, m)
            }
        }
    }
}

