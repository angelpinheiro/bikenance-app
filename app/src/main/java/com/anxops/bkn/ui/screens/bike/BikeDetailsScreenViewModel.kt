package com.anxops.bkn.ui.screens.bike

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.ComponentTypes
import com.anxops.bkn.data.model.MaintenanceConfigurations
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.ComponentRepositoryFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BikeDetailsScreenViewModel @Inject constructor(
    private val bikeComponentRepository: ComponentRepositoryFacade,
    private val bikesRepository: BikeRepositoryFacade,
    val db: AppDb,
    val api: Api,

    ) : ViewModel() {


    val selectedComponentTypes = mutableStateOf<Set<ComponentTypes>>(emptySet())

    private val selectedBikeId = MutableStateFlow<String?>(null)

    private val bikeFlow = selectedBikeId.mapLatest {
        it?.let { id -> bikesRepository.getBike(id) } ?: null
    }

    private val bikeComponentsFlow = bikeFlow.distinctUntilChanged().filterNotNull().map {
        bikeComponentRepository.getBikeComponentsFlow(bikeId = it._id)
    }.flattenConcat().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    val bike = bikeFlow.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val bikeComponents = bikeComponentsFlow.stateIn(
        viewModelScope, SharingStarted.Eagerly,
        emptyList()
    )

    fun loadBike(bikeId: String) {
        viewModelScope.launch {
            selectedBikeId.value = bikeId
        }
    }

    fun onSelectConfiguration(c: MaintenanceConfigurations) {
        viewModelScope.launch {
            selectedComponentTypes.value = c.types
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

                    if (it.virtualComponent != null) {
                        listOf(
                            BikeComponent(
                                bikeId = currentBike._id,
                                alias = "Front $alias",
                                type = it
                            ),
                            BikeComponent(
                                bikeId = currentBike._id,
                                alias = "Rear $alias",
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

}
