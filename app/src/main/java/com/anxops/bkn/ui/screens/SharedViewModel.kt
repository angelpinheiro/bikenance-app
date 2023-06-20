package com.anxops.bkn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class SharedEvent {
    object ProfileUpdated : SharedEvent()
    object None : SharedEvent()
}


@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    private val _eventFlow: MutableStateFlow<SharedEvent> = MutableStateFlow(SharedEvent.None)
    val eventFlow: StateFlow<SharedEvent> = _eventFlow

    fun notifyProfileUpdate() {
        viewModelScope.launch {
            _eventFlow.emit(SharedEvent.ProfileUpdated)
        }
    }

}
