package com.anxops.bkn.ui.screens.bikeSetup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.model.AthleteStats
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.BikeType
import com.anxops.bkn.data.model.ComponentModifier
import com.anxops.bkn.data.model.ComponentTypes
import com.anxops.bkn.data.model.getDefaultComponents
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.ComponentRepositoryFacade
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BikeSetupState(
    val bikeType: BikeType = BikeType.UNKNOWN,
    val componentTypes: List<ComponentTypes> = emptyList(),
    val stats: AthleteStats? = null
)

@HiltViewModel
class BikeSetupViewModel @Inject constructor(
    private val bikeComponentRepository: ComponentRepositoryFacade,
    private val bikesRepository: BikeRepositoryFacade,
    private val profileRepository: ProfileRepositoryFacade,
    val db: AppDb,
    val api: Api,

    ) : ViewModel() {


    val selectedComponentTypes = mutableStateOf<Set<ComponentTypes>>(emptySet())

    private val _state = MutableStateFlow(BikeSetupState())
    val state: StateFlow<BikeSetupState> = _state

    private val selectedBikeId = MutableStateFlow<String?>(null)

    private val bikeFlow = selectedBikeId.mapLatest {
        it?.let { id -> bikesRepository.getBike(id) } ?: null
    }

    val bike = bikeFlow.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun loadBike(bikeId: String) {
        viewModelScope.launch {
            selectedBikeId.value = bikeId
            profileRepository.getProfileStats()?.let {
                _state.value = _state.value.copy(stats = it)
            }
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

    fun addSelectedComponentsToBike() {
        viewModelScope.launch {

            bike.value?.let { currentBike ->
                val newComponents = selectedComponentTypes.value.map {

                    var alias = it.name
                    val cs = listOf(
                        ComponentTypes.DISC_BRAKE,
                        ComponentTypes.THRU_AXLE,
                        ComponentTypes.DISC_PAD,
                        ComponentTypes.TIRE
                    )
                    if (cs.contains(it)) {
                        listOf(
                            BikeComponent(
                                bikeId = currentBike._id,
                                modifier = ComponentModifier.FRONT,
                                alias = alias,
                                type = it
                            ),
                            BikeComponent(
                                bikeId = currentBike._id,
                                modifier = ComponentModifier.REAR,
                                alias = alias,
                                type = it
                            )
                        )
                    } else {
                        listOf(
                            BikeComponent(
                                bikeId = currentBike._id,
                                alias = alias,
                                type = it
                            )
                        )
                    }

                }.flatten()

                bikeComponentRepository.createComponents(currentBike._id, newComponents)
            }
        }
    }

    fun onBikeTypeSelected(it: BikeType) {
        viewModelScope.launch {
            _state.value = _state.value.copy(bikeType = it)
        }
    }

}
