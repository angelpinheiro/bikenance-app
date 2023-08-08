package com.anxops.bkn.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.entities.AppInfo
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.ApiResponse
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.repository.AppInfoRepositoryFacade
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import com.anxops.bkn.data.repository.onSuccess
import com.anxops.bkn.data.repository.successOrException
import com.anxops.bkn.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Duration
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class HomeEvent {
    object Logout : HomeEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStore: BknDataStore,
    private val api: Api,
    private val db: AppDb,
    private val profileRepository: ProfileRepositoryFacade,
    private val appInfoRepository: AppInfoRepositoryFacade
) : ViewModel() {

    val events: MutableSharedFlow<HomeEvent> = MutableSharedFlow()

    val allowRefreshState = db.appInfoDao().getAppInfoFlow().map { info ->
        info?.let { allowRefresh(it) } ?: false
    }.stateIn(viewModelScope, WhileUiSubscribed, false)

    val profileSyncState = profileRepository.getProfileFlow().map {
        it.successOrException { p -> p?.sync ?: false }
    }.stateIn(viewModelScope, WhileUiSubscribed, null)

    init {
        viewModelScope.launch {
            profileRepository.refreshProfile()
        }
    }

    fun logout() {
        viewModelScope.launch {
            profileRepository.logout().onSuccess {
                events.emit(HomeEvent.Logout)
            }
        }
    }

    fun refreshRides() {
        viewModelScope.launch {
            db.appInfoDao().getAppInfo()?.let { appInfo ->
                // only one req per day
                if (allowRefresh(appInfo)) {
                    when (val result = api.refreshLastRides()) {
                        is ApiResponse.Success -> {
                            appInfoRepository.saveLastRidesRefresh(System.currentTimeMillis())
                        }

                        is ApiResponse.Error -> {
                            throw Exception("$result")
                        }
                    }
                }
            }
        }
    }

    private fun allowRefresh(appInfo: AppInfo): Boolean {
        return Instant.ofEpochMilli(appInfo.lastRidesRefreshRequest).isBefore(Instant.now().minus(Duration.ofDays(1)))
    }
}
