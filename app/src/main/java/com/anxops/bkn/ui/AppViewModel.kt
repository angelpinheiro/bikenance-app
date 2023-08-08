package com.anxops.bkn.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.repeatOnLifecycle
import com.anxops.bkn.data.network.ConnectionState
import com.anxops.bkn.data.network.ConnectivityChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class AppViewModel @Inject constructor(
    private val connectivityChecker: ConnectivityChecker
) : ViewModel() {

    private val previousConnectionState = MutableStateFlow<ConnectionState?>(null)
    val displayConnectionState = MutableStateFlow<ConnectionState?>(null)

    fun subscribeToConnectivity(
        lifeCycleScope: LifecycleCoroutineScope,
        lifeCycle: Lifecycle
    ) {
        lifeCycleScope.launch {
            // Ensure we are not collecting the flow when the app is in background
            lifeCycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                connectivityChecker.connectivityAsFlow().distinctUntilChanged().collect { currentState ->
                    Timber.d("Connectivity change")
                    // Emit the current state on every change
                    displayConnectionState.emit(currentState)
                    // When transitioning from Unavailable to Available, show Available during 2 seconds
                    if (currentState is ConnectionState.Available &&
                        previousConnectionState.value is ConnectionState.Unavailable
                    ) {
                        delay(2000)
                        displayConnectionState.emit(null)
                    }
                    // Connection Available from Unknown, clear display
                    else if (currentState is ConnectionState.Available) {
                        displayConnectionState.emit(null)
                    }
                    // Store the current state for future comparison.
                    previousConnectionState.emit(currentState)
                }
            }
        }
    }
}
