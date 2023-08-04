package com.anxops.bkn.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Profile
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import com.anxops.bkn.data.repository.Result
import com.anxops.bkn.data.repository.onError
import com.anxops.bkn.data.repository.onSuccessNotNull
import com.anxops.bkn.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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

    private val statusFlow = MutableStateFlow<ProfileScreenStatus>(ProfileScreenStatus.Loading)
    private val profileFlow = MutableStateFlow(Profile.Empty)
    private val loadProgressFlow = MutableStateFlow(0.0f)

    val state: StateFlow<ProfileScreenState> = combine(
        statusFlow, loadProgressFlow, profileFlow
    ) { statusRes, loadProgressRes, profileRes ->
        ProfileScreenState(statusRes, loadProgressRes, profileRes)
    }.stateIn(viewModelScope, WhileUiSubscribed, ProfileScreenState())


    fun loadProfile() {
        viewModelScope.launch {
            profileRepository.getProfile().onSuccessNotNull {
                profileFlow.emit(it)
                statusFlow.emit(ProfileScreenStatus.Loaded)
            }.onError {
                //TODO
            }
        }
    }

    fun updateFirstname(value: String) {
        profileFlow.update { profileFlow.value.copy(firstname = value) }
    }

    fun updateLastname(value: String) {
        profileFlow.update { profileFlow.value.copy(lastname = value) }
    }

    fun saveProfileChanges() {

        viewModelScope.launch {

            statusFlow.update { ProfileScreenStatus.Loading }

            when (val result = profileRepository.updateProfile(profileFlow.value)) {

                is Result.Success -> {
                    profileFlow.update { result.data }
                    statusFlow.update { ProfileScreenStatus.UpdateSuccess }
                }

                else -> {
                    statusFlow.update { ProfileScreenStatus.UpdateSuccess }
                }
            }
        }
    }


    fun onUpdateProfileImage(bytes: ByteArray) {
        viewModelScope.launch {
            loadProgressFlow.update { 0f }
            api.uploadImageToFirebase(dataStore.getAuthUserOrFail(),
                bytes,
                onUpdateUpload = { updatePercent ->
                    loadProgressFlow.update { updatePercent }
                },
                onSuccess = { url ->
                    loadProgressFlow.update { 0f }
                    profileFlow.update { it.copy(profilePhotoUrl = url) }
                },
                onFailure = {
                    statusFlow.update { ProfileScreenStatus.Error }
                })
        }
    }

}