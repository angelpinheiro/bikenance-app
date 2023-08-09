package com.anxops.bkn.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.network.ConnectionState
import com.anxops.bkn.data.network.ConnectivityChecker
import com.anxops.bkn.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

sealed interface DisplayConnectionState {
    object DisplayOnline : DisplayConnectionState
    object DisplayOffline : DisplayConnectionState
    object DisplayNone : DisplayConnectionState
}

@HiltViewModel
class AppViewModel @Inject constructor(
    private val connectivityChecker: ConnectivityChecker
) : ViewModel() {

    private val previousConnectionState = MutableStateFlow<ConnectionState?>(null)
    val displayConnectionState = createDisplayConnectionFlow().stateIn(
        viewModelScope,
        WhileUiSubscribed,
        DisplayConnectionState.DisplayNone
    )

    private fun createDisplayConnectionFlow(): Flow<DisplayConnectionState> {
        return flow {
            connectivityChecker.connectivityAsFlow().distinctUntilChanged().collect { currentState ->
                Timber.d("Connectivity change")
                if (currentState is ConnectionState.Available) {
                    // Emit Online state when connection is Available
                    emit(DisplayConnectionState.DisplayOnline)
                    // Check if transitioning from Unavailable to Available
                    if (previousConnectionState.value is ConnectionState.Unavailable) {
                        delay(2000)
                        emit(DisplayConnectionState.DisplayNone)
                    } else {
                        emit(DisplayConnectionState.DisplayNone)
                    }
                } else {
                    // Emit Offline state when connection is not Available
                    emit(DisplayConnectionState.DisplayOffline)
                }
                // Store the current state for future comparison
                previousConnectionState.emit(currentState)
            }
        }
    }
}
