package com.anxops.bkn.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.network.ApiEndpoints
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class LoginEvent {
    object StravaLogin : LoginEvent()
    class EmailPasswordLogin(val email: String, val password: String) : LoginEvent()
}

data class LoginScreenState(
    val email: String = "",
    val password: String = ""
)

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    val dataStore: BknDataStore
) : ViewModel() {

    private var _state = MutableStateFlow(LoginScreenState())
    val state: StateFlow<LoginScreenState> = _state

    val loginEvent: MutableSharedFlow<LoginEvent> = MutableSharedFlow()

    val useDebugApi = dataStore.useDebugAPi.stateIn(viewModelScope, WhileUiSubscribed, true).map { it ?: false }

    fun updateEmail(value: String) {
        _state.value = _state.value.copy(email = value)
    }

    fun updatePassword(value: String) {
        _state.value = _state.value.copy(password = value)
    }

    fun signInWithEmailAndPassword() {
        viewModelScope.launch {
            loginEvent.emit(
                LoginEvent.EmailPasswordLogin(_state.value.email, _state.value.password)
            )
        }
    }

    fun signInWithStrava() {
        viewModelScope.launch {
            loginEvent.emit(LoginEvent.StravaLogin)
        }
    }

    fun setUseDebugApi(use: Boolean) {
        viewModelScope.launch {
            dataStore.setUseDebugAPi(use)
            ApiEndpoints.setUseDebugAPi(use)
        }
    }
}
