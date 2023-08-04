package com.anxops.bkn.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Profile
import com.anxops.bkn.data.network.firebase.SendTokenToServerWorkerStarter
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import com.anxops.bkn.data.repository.onError
import com.anxops.bkn.data.repository.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
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

            profileRepository.saveLogin(token, refreshToken).onSuccess { p ->
                sendFirebaseTokenToServer.start()

                bikeRepository.refreshBikes()
                ridesRepository.refreshRides()

                _state.update {
                    it.copy(profile = p, isNewAccount = p.isNew())
                }

                if (p.isNew()) {
                    loadProfileEvent.emit(LoadProfileEvent.NewAccount)
                } else {
                    loadProfileEvent.emit(LoadProfileEvent.ExistingAccount)
                }

            }.onError {
                // TODO: Handle error
                loadProfileEvent.emit(LoadProfileEvent.LoadFailed)
            }
        }
    }

}
