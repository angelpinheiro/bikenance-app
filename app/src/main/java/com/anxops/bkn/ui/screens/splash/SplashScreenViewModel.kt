package com.anxops.bkn.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.network.ApiEndpoints
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.repository.CheckLoginResult
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import com.anxops.bkn.data.repository.onError
import com.anxops.bkn.data.repository.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class CheckLoginState {
    object LoggedIn : CheckLoginState()
    object NotLoggedIn : CheckLoginState()
    object LoginExpired : CheckLoginState()
    object Checking : CheckLoginState()
}

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    val dataStore: BknDataStore,
    private val repository: ProfileRepositoryFacade
) : ViewModel() {

    init {
        viewModelScope.launch {
            ApiEndpoints.setUseDebugAPi(dataStore.getUseDebugAPi() ?: false)
        }
    }

    val isLogged = flow {
        repository.checkLogin().onSuccess { checkResult ->
            val state = when (checkResult) {
                is CheckLoginResult.LoggedIn -> CheckLoginState.LoggedIn
                is CheckLoginResult.LoginExpired -> CheckLoginState.LoginExpired
                else -> CheckLoginState.NotLoggedIn
            }
            emit(state)
        }.onError {
            // TODO handle error
            emit(CheckLoginState.NotLoggedIn)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), CheckLoginState.Checking)
}
