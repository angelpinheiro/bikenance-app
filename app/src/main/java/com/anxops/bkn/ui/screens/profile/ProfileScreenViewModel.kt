package com.anxops.bkn.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Profile
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import com.anxops.bkn.util.RepositoryResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class ProfileScreenStatus {
    object Loading : ProfileScreenStatus()
    object Saving : ProfileScreenStatus()
    object Loaded : ProfileScreenStatus()
    object UpdateSuccess : ProfileScreenStatus()
    object Error : ProfileScreenStatus()
}

data class ProfileScreenState(
    val status: ProfileScreenStatus = ProfileScreenStatus.Loading,
    val profileImagePercent: Float = 0f,
    val profile: Profile = Profile.Empty,
)

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val api: Api,
    private val dataStore: BknDataStore,
    private val profileRepository: ProfileRepositoryFacade,
) : ViewModel() {

    val updateEvent: MutableSharedFlow<Boolean> = MutableSharedFlow()

    private val _state = MutableStateFlow(ProfileScreenState())
    val state: StateFlow<ProfileScreenState> = _state


    fun loadProfile() {
        viewModelScope.launch {
            val profile = profileRepository.getProfile()
            if (profile != null) {
                _state.value = _state.value.copy(
                    profile = profile, status = ProfileScreenStatus.Loaded
                )
            }
        }

    }

    fun updateFirstname(value: String) {
        _state.value = _state.value.copy(
            profile = _state.value.profile.copy(firstname = value)
        )
    }

    fun updateLastname(value: String) {
        _state.value = _state.value.copy(
            profile = _state.value.profile.copy(lastname = value)
        )
    }

    fun saveProfileChanges() {

        viewModelScope.launch {

            _state.value = _state.value.copy(
                status = ProfileScreenStatus.Loading
            )

            when (val result = profileRepository.updateProfile(_state.value.profile)) {

                is RepositoryResult.Success -> {
                    _state.value = _state.value.copy(
                        profile = result.data, status = ProfileScreenStatus.UpdateSuccess
                    )
                }

                else -> {
                    _state.value = _state.value.copy(
                        status = ProfileScreenStatus.Error
                    )
                }
            }
        }
    }


    fun onUpdateProfileImage(imageByteArray: ByteArray?) {
        viewModelScope.launch {
            imageByteArray?.let {
                api.uploadImageToFirebase(dataStore.getAuthUserOrFail(),
                    it,
                    onUpdateUpload = { updatePercent ->
                        _state.value = _state.value.copy(profileImagePercent = updatePercent)
                    },
                    onSuccess = { url ->
                        _state.value = _state.value.copy(
                            profileImagePercent = 0f, profile = _state.value.profile.copy(
                                profilePhotoUrl = url,
                            )
                        )
                    },
                    onFailure = {
                        _state.value = _state.value.copy(
                            status = ProfileScreenStatus.Error
                        )
                    })
            }
        }
    }

}