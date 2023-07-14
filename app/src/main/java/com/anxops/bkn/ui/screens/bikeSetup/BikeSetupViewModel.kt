package com.anxops.bkn.ui.screens.bikeSetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.model.AthleteStats
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.BikeType
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.data.model.ComponentModifier
import com.anxops.bkn.data.model.ComponentTypes
import com.anxops.bkn.data.model.maintenanceConfigurations
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.ComponentRepositoryFacade
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class BSSEvent {
    data class BikeTypeSelected(val type: BikeType) : BSSEvent()
    data class LastMaintenanceUpdate(val category: ComponentCategory, val value: Float) :
        BSSEvent()

    data class CliplessPedalsSelectionChange(val value: Boolean) : BSSEvent()
    data class TubelessSelectionChange(val value: Boolean) : BSSEvent()
    data class DropperSelectionChange(val value: Boolean) : BSSEvent()
}

sealed class BikeSetupScreenState {
    object Loading : BikeSetupScreenState()
    object SavingSetup : BikeSetupScreenState()
    data class SetupDone(val bike: Bike) : BikeSetupScreenState()
    data class SetupInProgress(
        val bike: Bike, val stats: AthleteStats, val details: SetupDetails = SetupDetails()
    ) : BikeSetupScreenState()

    class Error(val text: String) : BikeSetupScreenState()
}

data class SetupDetails(
    val selectedBikeType: BikeType = BikeType.MTB,
    val hasDropperPost: Boolean? = false,
    val hasTubeless: Boolean? = true,
    val hasCliplessPedals: Boolean? = true,
    val lastMaintenances: Map<ComponentCategory, Float> = ComponentCategory.values().toList()
        .minus(ComponentCategory.MISC).associateWith { 0f }

)

@HiltViewModel
class BikeSetupViewModel @Inject constructor(
    private val bikeComponentRepository: ComponentRepositoryFacade,
    private val bikesRepository: BikeRepositoryFacade,
    private val profileRepository: ProfileRepositoryFacade,
    val db: AppDb,
    val api: Api,

    ) : ViewModel() {


    val _state = MutableStateFlow<BikeSetupScreenState>(BikeSetupScreenState.Loading)
    val state: StateFlow<BikeSetupScreenState> = _state


    fun loadBike(bikeId: String) {
        viewModelScope.launch {
            val bike = bikesRepository.getBike(bikeId)
            val stats = profileRepository.getProfileStats()

            if (bike != null && stats != null) {
                _state.value = BikeSetupScreenState.SetupInProgress(
                    bike = bike, stats = stats
                )
            } else {
                _state.value = BikeSetupScreenState.Error("Could not load data. Try again later.")
            }
        }
    }

    fun onFinishBikeSetup() = viewModelScope.launch {
        state.value.let { currentState ->
            when (currentState) {
                is BikeSetupScreenState.SetupInProgress -> {

                    _state.value = BikeSetupScreenState.SavingSetup

                    val bike = currentState.bike.copy(type = currentState.details.selectedBikeType, configDone = true)
                    val newComponents = getNewComponentsForBike(
                        bike,
                        currentState.details.hasDropperPost ?: false,
                        currentState.details.hasCliplessPedals ?: false
                    )
                    bikeComponentRepository.createComponents(bike._id, newComponents)
                    bikesRepository.updateBike(bike)

                    _state.value = BikeSetupScreenState.SetupDone(bike)
                }

                else -> {
                    // do nothing
                }
            }
        }
    }

    fun onSetupDetailsEvent(event: BSSEvent) {

        _state.value = when (val currentState = _state.value) {
            is BikeSetupScreenState.SetupInProgress -> {
                currentState.copy(
                    details = when (event) {
                        is BSSEvent.BikeTypeSelected -> {
                            currentState.details.copy(
                                selectedBikeType = event.type
                            )
                        }

                        is BSSEvent.CliplessPedalsSelectionChange -> {
                            currentState.details.copy(
                                hasCliplessPedals = event.value
                            )

                        }

                        is BSSEvent.DropperSelectionChange -> {
                            currentState.details.copy(
                                hasDropperPost = event.value
                            )

                        }

                        is BSSEvent.TubelessSelectionChange -> {
                            currentState.details.copy(
                                hasTubeless = event.value
                            )
                        }

                        is BSSEvent.LastMaintenanceUpdate -> currentState.details.copy(
                            lastMaintenances = currentState.details.lastMaintenances.plus(event.category to event.value)
                        )
                    }
                )
            }

            else -> {
                currentState
            }
        }

    }

    private fun getNewComponentsForBike(
        bike: Bike, includeDropper: Boolean, includePedals: Boolean
    ): List<BikeComponent> {

        val componentTypes = maintenanceConfigurations[bike.type]!!.toMutableList()

        if (includeDropper) {
            componentTypes.add(ComponentTypes.DROPER_POST)
        }
        if (includePedals) {
            componentTypes.add(ComponentTypes.PEDAL_CLIPLESS)
        }

        return componentTypes.map {

            var alias = it.name
            val duplicatedComponents = listOf(
                ComponentTypes.DISC_BRAKE,
                ComponentTypes.THRU_AXLE,
                ComponentTypes.DISC_PAD,
                ComponentTypes.TIRE
            )
            if (duplicatedComponents.contains(it)) {
                listOf(
                    BikeComponent(
                        bikeId = bike._id,
                        modifier = ComponentModifier.FRONT,
                        alias = alias,
                        type = it
                    ), BikeComponent(
                        bikeId = bike._id,
                        modifier = ComponentModifier.REAR,
                        alias = alias,
                        type = it
                    )
                )
            } else {
                listOf(
                    BikeComponent(
                        bikeId = bike._id, alias = alias, type = it
                    )
                )
            }

        }.flatten()
    }
}
