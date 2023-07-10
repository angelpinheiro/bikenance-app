package com.anxops.bkn.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.network.ApiEndpoints
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CheckLoginState {
    object LoggedIn : CheckLoginState()
    object NotLoggedIn : CheckLoginState()
    object LoginExpired : CheckLoginState()
    object Checking : CheckLoginState()
}

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    val dataStore: BknDataStore,
    private val repository: ProfileRepositoryFacade,
) : ViewModel() {

    init {
        viewModelScope.launch {
            ApiEndpoints.setUseDebugAPi(dataStore.getUseDebugAPi() ?: false)
        }
    }

    val isLogged = dataStore.authToken.map { token ->
        val profile = repository.getProfile()
        if (profile == null) {
            CheckLoginState.NotLoggedIn
        } else if (token != null) {
            // TODO: check token is valid or refresh token
            CheckLoginState.LoggedIn
        } else {
            CheckLoginState.LoginExpired
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), CheckLoginState.Checking)

}
