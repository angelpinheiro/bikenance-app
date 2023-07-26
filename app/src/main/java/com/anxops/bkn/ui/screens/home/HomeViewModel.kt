package com.anxops.bkn.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.entities.AppInfo
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.ApiResponse
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStore: BknDataStore,
    private val api: Api,
    private val db: AppDb,
    private val profileRepository: ProfileRepositoryFacade,
) : ViewModel() {

    val allowRefreshState = db.appInfoDao().getAppInfoFlow().map {info ->
        info?.let { allowRefresh(it) } ?: false
    }.stateIn(viewModelScope, SharingStarted.Eagerly ,false)

    val profileSyncState = profileRepository.getProfileFlow().map {
        it?.sync
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        viewModelScope.launch {
            profileRepository.reloadData()
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStore.deleteAuthToken()
        }
    }

    fun refreshRides() {
        viewModelScope.launch {
            db.appInfoDao().getAppInfo()?.let {  appInfo ->
                // only one req per day
                if (allowRefresh(appInfo)) {

                    when (val result = api.refreshLastRides()) {
                        is ApiResponse.Success -> {
                            db.appInfoDao().clear()
                            db.appInfoDao().insert(appInfo.copy(lastRidesRefreshRequest = System.currentTimeMillis()))
                            Log.d("RidesScreenViewModel", "RefreshLastRides Success: ${result.data}")
                        }

                        is ApiResponse.Error -> {
                            throw Exception("${result.message}")
                        }
                    }
                }

            }
        }
    }


    private fun allowRefresh(appInfo: AppInfo): Boolean {
        return Instant.ofEpochMilli(appInfo.lastRidesRefreshRequest)
            .isBefore(Instant.now().minus(Duration.ofDays(1)))
    }
}