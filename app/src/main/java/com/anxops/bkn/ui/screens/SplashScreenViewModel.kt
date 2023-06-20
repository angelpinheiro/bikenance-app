package com.anxops.bkn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.storage.BknDataStore
import com.anxops.bkn.storage.ProfileRepositoryFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed class CheckLoginState {
    object Success : CheckLoginState()
    object Failure : CheckLoginState()
    object Checking : CheckLoginState()
}

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    val dataStore: BknDataStore,
    private val repository: ProfileRepositoryFacade,
) : ViewModel() {

    val isLogged = dataStore.authToken.map {
        if (it != null && repository.getProfile() != null) CheckLoginState.Success else CheckLoginState.Failure
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), CheckLoginState.Checking)

}
