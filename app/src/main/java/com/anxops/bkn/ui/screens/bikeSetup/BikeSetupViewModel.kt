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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


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
    val wearLevel: Map<ComponentCategory, Float> = ComponentCategory.values().toList()
        .minus(ComponentCategory.MISC).associateWith { 0.5f },

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

    fun finishBikeSetup() = viewModelScope.launch {
        state.value.let {
            when (it) {
                is BikeSetupScreenState.SetupInProgress -> {

                    _state.value = BikeSetupScreenState.SavingSetup

                    val bike = it.bike.copy(type = it.details.selectedBikeType, configDone = true)
                    val newComponents = getNewComponentsForBike(
                        bike,
                        it.details.hasDropperPost ?: false,
                        it.details.hasCliplessPedals ?: false
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

    fun onEvent(event: BikeSetupScreenEvent) {

        _state.value.let { currentState ->
            if (currentState is BikeSetupScreenState.SetupInProgress) {

                _state.update {
                    when (event) {
                        is BikeSetupScreenEvent.BikeTypeSelected -> {
                            currentState.copy(
                                details = currentState.details.copy(
                                    selectedBikeType = event.type
                                )
                            )
                        }

                        is BikeSetupScreenEvent.WearLevelUpdate -> {
                            currentState.copy(
                                details = currentState.details.copy(
                                    wearLevel = currentState.details.wearLevel.plus(event.category to event.value)
                                )
                            )
                        }

                        is BikeSetupScreenEvent.CliplessPedalsSelectionChange -> {
                            currentState.copy(
                                details = currentState.details.copy(
                                    hasCliplessPedals = event.value
                                )
                            )
                        }

                        is BikeSetupScreenEvent.DropperSelectionChange -> {
                            currentState.copy(
                                details = currentState.details.copy(
                                    hasDropperPost = event.value
                                )
                            )
                        }

                        is BikeSetupScreenEvent.TubelessSelectionChange -> {
                            currentState.copy(
                                details = currentState.details.copy(
                                    hasTubeless = event.value
                                )
                            )
                        }

                        else -> {
                            currentState
                        }
                    }
                }
            }
        }
    }

    fun onBikeTypeSelected(bikeType: BikeType) {
        _state.value.let { currentState ->
            if (currentState is BikeSetupScreenState.SetupInProgress) {
                _state.update {
                    currentState.copy(
                        details = currentState.details.copy(
                            selectedBikeType = bikeType
                        )
                    )
                }
            }
        }
    }

    fun onWearLevelUpdate(c: ComponentCategory, value: Float) {

        _state.value.let { currentState ->
            if (currentState is BikeSetupScreenState.SetupInProgress) {
                _state.update {
                    currentState.copy(
                        details = currentState.details.copy(
                            wearLevel = currentState.details.wearLevel.plus(c to value)
                        )
                    )
                }
            }
        }
    }

    fun onLastMaintenanceUpdate(c: ComponentCategory, months: Float) {

        _state.value.let { currentState ->
            if (currentState is BikeSetupScreenState.SetupInProgress) {
                _state.update {
                    currentState.copy(
                        details = currentState.details.copy(
                            lastMaintenances = currentState.details.lastMaintenances.plus(c to months)
                        )
                    )
                }
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

    fun onCliplessPedalsSelectionChange(selection: Boolean) {
        _state.value.let { currentState ->
            if (currentState is BikeSetupScreenState.SetupInProgress) {
                _state.update {
                    currentState.copy(
                        details = currentState.details.copy(
                            hasCliplessPedals = selection
                        )
                    )
                }
            }
        }
    }

    fun onDropperSelectionChange(selection: Boolean) {
        _state.value.let { currentState ->
            if (currentState is BikeSetupScreenState.SetupInProgress) {
                _state.update {
                    currentState.copy(
                        details = currentState.details.copy(
                            hasDropperPost = selection
                        )
                    )
                }
            }
        }
    }

    fun onTubelessSelectionChange(selection: Boolean) {
        _state.value.let { currentState ->
            if (currentState is BikeSetupScreenState.SetupInProgress) {
                _state.update {
                    currentState.copy(
                        details = currentState.details.copy(
                            hasTubeless = selection
                        )
                    )
                }
            }
        }
    }

}
