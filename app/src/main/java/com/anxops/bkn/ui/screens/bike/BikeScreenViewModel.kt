package com.anxops.bkn.ui.screens.bike

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
) : ViewModel() {

    private val stateFlow = MutableStateFlow(BikeScreenState())
    val state: StateFlow<BikeScreenState> = stateFlow

    val openBikeOnStravaEvent: MutableSharedFlow<String> = MutableSharedFlow()

    private fun openBikeOnStrava(id: String) {
        viewModelScope.launch {
            openBikeOnStravaEvent.emit(
                id
            )
        }
    }


    fun handleEvent(event: BikeScreenEvent) = viewModelScope.launch {

        val newState: BikeScreenState = when (event) {
            is BikeScreenEvent.SelectComponent -> {
                state.value.copy(
                    selectedComponent = selectedComp(state.value.selectedComponent, event.component)
                )
            }

            is BikeScreenEvent.SelectComponentCategory -> {
                state.value.copy(
                    selectedCategory = selectedCat(state.value.selectedCategory, event.category),
                    selectedComponent = null
                )
            }

            is BikeScreenEvent.LoadBike -> {

                val bike = bikesRepository.getBike(event.bikeId)
                val component = bike?.components?.find { event.componentId == it._id }

                state.value.copy(
                    bike = bike,
                    selectedComponent = component,
                    selectedCategory = component?.type?.category
                )
            }

            is BikeScreenEvent.ViewOnStrava -> {
                state.value.bike?.stravaId?.let { openBikeOnStrava(it) }
                state.value
            }
        }
        if (newState != state.value) {
            stateFlow.update { newState }
        }
    }

    private fun selectedCat(curr: ComponentCategory?, new: ComponentCategory?): ComponentCategory? {
        return if (curr != new) new else null
    }

    private fun selectedComp(curr: BikeComponent?, new: BikeComponent?): BikeComponent? {
        return if (curr != new) new else null
    }
}


