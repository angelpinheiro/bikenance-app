/*
 * Copyright 2023 Marco Cattaneo
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

package com.anxops.bkn.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.network.Api
import com.anxops.bkn.network.tokenRefresh.RefreshTokenHelper
import com.anxops.bkn.storage.BknDataStore
import com.anxops.bkn.storage.DBSynchronizer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStore: BknDataStore,
    private val dbSync: DBSynchronizer,
    private val api: Api
) : ViewModel() {

    init {
        // Refresh db
        viewModelScope.launch {
            dbSync.refreshProfileAndBikesInTransaction(viewModelScope)
        }
    }

    fun refreshToken() {
        viewModelScope.launch {
            RefreshTokenHelper.performRefresh(dataStore, api)
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStore.deleteAuthToken()
        }
    }
}