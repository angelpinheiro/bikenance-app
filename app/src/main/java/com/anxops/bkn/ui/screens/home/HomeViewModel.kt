package com.anxops.bkn.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.DBSynchronizer
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.tokenRefresh.RefreshTokenHelper
import com.anxops.bkn.data.preferences.BknDataStore
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