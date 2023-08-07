package com.anxops.bkn.ui.screens.bike

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.onError
import com.anxops.bkn.data.repository.onSuccess
import com.anxops.bkn.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class BikeScreenEvent {
    object ViewOnStrava : BikeScreenEvent()
    data class LoadBike(val bikeId: String, val componentId: String? = null) : BikeScreenEvent()
    data class SelectComponent(val component: BikeComponent?) : BikeScreenEvent()
    data class SelectComponentCategory(val category: ComponentCategory?) : BikeScreenEvent()
}

data class BikeScreenState(
    val loading: Boolean = true,
    val bike: Bike? = null,
    val selectedCategory: ComponentCategory? = null,
    val selectedComponent: BikeComponent? = null
)

@HiltViewModel
class BikeScreenViewModel @Inject constructor(
    private val bikesRepository: BikeRepositoryFacade,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val openBikeOnStravaEvent: MutableSharedFlow<String> = MutableSharedFlow()

    private val bikeFlow = MutableStateFlow<Bike?>(null)
    private val loadingFlow = MutableStateFlow(false)
    private val selectedCategoryFLow = MutableStateFlow<ComponentCategory?>(null)
    private val selectedComponentFlow = MutableStateFlow<BikeComponent?>(null)

    val state: StateFlow<BikeScreenState> = combine(
        loadingFlow, bikeFlow, selectedComponentFlow, selectedCategoryFLow
    ) { loading, bike, selectedComponent, selectedCategory ->


        val lastSelected = savedStateHandle.get<String>("cid")
        val selected = if (selectedComponent == null && lastSelected != null) {
            bike?.components?.find { it._id == lastSelected }.let {
                it
            }
        } else selectedComponent

        BikeScreenState(
            loading = loading,
            bike = bike,
            selectedCategory = selectedCategory,
            selectedComponent = selected
        )
    }.stateIn(viewModelScope, WhileUiSubscribed, BikeScreenState())


    private fun openBikeOnStrava(id: String) {
        viewModelScope.launch {
            openBikeOnStravaEvent.emit(
                id
            )
        }
    }

    fun handleEvent(event: BikeScreenEvent) = viewModelScope.launch {
        when (event) {
            is BikeScreenEvent.SelectComponent -> {
                selectedComponentFlow.update {
                    savedStateHandle["cid"] = event.component?._id
                    selectedComp(selectedComponentFlow.value, event.component)

                }
            }

            is BikeScreenEvent.SelectComponentCategory -> {
                selectedCategoryFLow.update {
                    selectedCat(selectedCategoryFLow.value, event.category)
                }
            }

            is BikeScreenEvent.LoadBike -> {
                bikesRepository.getBike(event.bikeId).onSuccess { bike ->
                    val component = bike.components?.find { event.componentId == it._id }
                    bikeFlow.update { bike }
                    selectedComponentFlow.update { component }
                    selectedCategoryFLow.update { component?.type?.category }
                }.onError {
                    // TODO: Handle error
                }
            }

            is BikeScreenEvent.ViewOnStrava -> {
                bikeFlow.value?.stravaId?.let { openBikeOnStrava(it) }
            }
        }
    }

    private fun selectedCat(curr: ComponentCategory?, new: ComponentCategory?): ComponentCategory? {
        return if (curr != new) new else null
    }

    private fun selectedComp(curr: BikeComponent?, new: BikeComponent?): BikeComponent? {
        return if (curr != new) new else null
    }
}


