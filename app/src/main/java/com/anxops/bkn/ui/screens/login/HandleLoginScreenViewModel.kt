package com.anxops.bkn.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Profile
import com.anxops.bkn.data.network.firebase.SendTokenToServerWorkerStarter
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import com.anxops.bkn.data.repository.onSuccessNotNull
import com.anxops.bkn.data.repository.onSuccessWithNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class HandleLoginScreenState(
    val profile: Profile? = null, val isNewAccount: Boolean? = null
)

sealed class LoadProfileEvent {
    object ExistingAccount : LoadProfileEvent()
    object NewAccount : LoadProfileEvent()
    object LoadFailed : LoadProfileEvent()
}

@HiltViewModel
class HandleLoginScreenViewModel @Inject constructor(
    private val dataStore: BknDataStore,
    private val profileRepository: ProfileRepositoryFacade,
    private val bikeRepository: BikeRepositoryFacade,
    private val ridesRepository: RidesRepositoryFacade,
    private val sendFirebaseTokenToServer: SendTokenToServerWorkerStarter
) : ViewModel() {

    private var _state = MutableStateFlow(HandleLoginScreenState())
    val state: StateFlow<HandleLoginScreenState> = _state
    val loadProfileEvent: MutableSharedFlow<LoadProfileEvent> = MutableSharedFlow()

    fun setAuthTokens(code: String?, refreshToken: String?) {
        code?.let { loadUserProfile(it, refreshToken) }
    }

    private fun loadUserProfile(token: String, refreshToken: String?) {
        viewModelScope.launch {

            dataStore.saveAuthTokens(token, refreshToken ?: "")
            sendFirebaseTokenToServer.start()

            profileRepository.refreshProfile()

            val result = profileRepository.getProfile()

            result.onSuccessNotNull { profile ->
                dataStore.saveAuthUser(profile.userId)
                val isNewAccount = profile.createdAt == null
                _state.update {
                    it.copy(
                        profile = profile, isNewAccount = isNewAccount
                    )
                }

                profileRepository.refreshProfile()
                bikeRepository.refreshBikes()
                ridesRepository.refreshRides()

                if (isNewAccount) {
                    delay(1000)
                    loadProfileEvent.emit(LoadProfileEvent.NewAccount)
                } else {
                    delay(500)
                    loadProfileEvent.emit(LoadProfileEvent.ExistingAccount)
                }
            }

            result.onSuccessWithNull {
                dataStore.deleteAuthToken()
                loadProfileEvent.emit(LoadProfileEvent.LoadFailed)
            }
        }
    }

}
