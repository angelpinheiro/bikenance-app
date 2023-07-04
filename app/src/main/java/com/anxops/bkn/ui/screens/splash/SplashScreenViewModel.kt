/*
 * Copyright 2023 Angel Piñeiro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anxops.bkn.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    val isLogged = dataStore.authToken.map { token ->
        val profile = repository.getProfile()
        if(profile == null) {
            CheckLoginState.NotLoggedIn
        }else if(token != null) {
            // TODO: check token is valid or refresh token
            CheckLoginState.LoggedIn
        }else {
            CheckLoginState.LoginExpired
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), CheckLoginState.Checking)

}
