package com.anxops.bkn.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStore: BknDataStore,
    private val profileRepositoryFacade: ProfileRepositoryFacade,
) : ViewModel() {

    init {
        // Refresh db
        viewModelScope.launch {
            profileRepositoryFacade.reloadData()
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStore.deleteAuthToken()
        }
    }
}