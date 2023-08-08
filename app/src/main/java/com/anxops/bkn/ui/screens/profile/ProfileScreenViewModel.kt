package com.anxops.bkn.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Profile
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.ImageUploadRequest
import com.anxops.bkn.data.network.ImageUploader
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.repository.AppError
import com.anxops.bkn.data.repository.ErrorType
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import com.anxops.bkn.data.repository.Result
import com.anxops.bkn.data.repository.onError
import com.anxops.bkn.data.repository.onSuccess
import com.anxops.bkn.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class ProfileScreenStatus {
    object Loading : ProfileScreenStatus()
    object Saving : ProfileScreenStatus()
    object Loaded : ProfileScreenStatus()
    object UpdateSuccess : ProfileScreenStatus()
}

data class ProfileScreenState(
    val status: ProfileScreenStatus = ProfileScreenStatus.Loading,
    val profileImagePercent: Float = 0f,
    val profile: Profile = Profile.Empty,
    val error: AppError? = null,
    val logoutRequested: Boolean = false
)

sealed class ProfileScreenEvent {
    object Logout : ProfileScreenEvent()
}

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val api: Api,
    private val dataStore: BknDataStore,
    private val profileRepository: ProfileRepositoryFacade,
    private val imageUploader: ImageUploader
) : ViewModel() {

    private val statusFlow = MutableStateFlow<ProfileScreenStatus>(ProfileScreenStatus.Loading)
    private val profileFlow = MutableStateFlow(Profile.Empty)
    private val loadProgressFlow = MutableStateFlow(0.0f)
    private val errorStateFlow = MutableStateFlow<AppError?>(null)

    private val eventFlow = MutableStateFlow<ProfileScreenEvent?>(null)
    val events = eventFlow.asStateFlow()

    val state: StateFlow<ProfileScreenState> = combine(
        statusFlow,
        loadProgressFlow,
        profileFlow,
        errorStateFlow
    ) { statusRes, loadProgressRes, profileRes, error ->
        ProfileScreenState(statusRes, loadProgressRes, profileRes, error)
    }.stateIn(viewModelScope, WhileUiSubscribed, ProfileScreenState())

    fun loadProfile() {
        viewModelScope.launch {
            profileRepository.getProfile().onSuccess {
                profileFlow.emit(it)
                statusFlow.emit(ProfileScreenStatus.Loaded)
            }.onError {
                // TODO
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

                is Result.Error -> {
                    statusFlow.update { ProfileScreenStatus.Loaded }
                    errorStateFlow.update { result.toAppError("Could not save changes") }
                }
            }
        }
    }

    fun onUpdateProfileImage(bytes: ByteArray) {
        viewModelScope.launch {
            loadProgressFlow.update { 0f }
            imageUploader.uploadImage(
                ImageUploadRequest(
                dataStore.getAuthUserOrFail(),
                    bytes
            ),
                onProgressUpdate = { updatePercent ->
                loadProgressFlow.update { updatePercent }
            },
                onSuccess = { url ->
                loadProgressFlow.update { 0f }
                profileFlow.update { it.copy(profilePhotoUrl = url) }
            },
                onFailure = {
                errorStateFlow.update { AppError(ErrorType.Unexpected, message = "Could not process the selected image") }
            }
            )
        }
    }

    fun onLogoutRequest() {
        viewModelScope.launch {
            profileRepository.logout().onSuccess {
                eventFlow.emit(ProfileScreenEvent.Logout)
            }
        }
    }

    fun onDismissError() {
        errorStateFlow.update { null }
    }
}
