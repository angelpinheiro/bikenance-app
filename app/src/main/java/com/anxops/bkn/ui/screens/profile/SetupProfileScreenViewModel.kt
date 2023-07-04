package com.anxops.bkn.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.Profile
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import com.anxops.bkn.util.RepositoryResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class SetupProfileScreenStatus {
    object LoadingProfile : SetupProfileScreenStatus()
    object SavingProfile : SetupProfileScreenStatus()
    object DisplayingProfile : SetupProfileScreenStatus()
}

data class SetupProfileScreenState(
    val bikes: List<Bike> = emptyList(),
    val status: SetupProfileScreenStatus = SetupProfileScreenStatus.LoadingProfile,
    val profileImagePercent: Float = 0f
)

@HiltViewModel
class SetupProfileScreenViewModel @Inject constructor(
    private val api: Api,
    private val dataStore: BknDataStore,
    private val profileRepository: ProfileRepositoryFacade,
    private val bikeRepository: BikeRepositoryFacade
) :
    ViewModel() {

    private val _state = MutableStateFlow(SetupProfileScreenState())
    val updateEvent: MutableSharedFlow<Boolean> = MutableSharedFlow()

    val state: StateFlow<SetupProfileScreenState> = _state

    private val _profileState: MutableStateFlow<Profile?> = MutableStateFlow(null)
    val profileState: StateFlow<Profile?> = _profileState

    init {
        loadUserProfile()
    }

    fun updateFirstname(value: String) {
        _profileState.value = _profileState.value?.copy(
            firstname = value
        )
    }

    fun updateLastname(value: String) {
        _profileState.value = _profileState.value?.copy(
            lastname = value
        )
    }

    fun syncBike(id: String) {

        val updatedBikeList = _state.value.bikes.toList().map {
            Log.d("VM", "${it._id} == $id")
            if (it._id == id) {
                it.copy(draft = !it.draft)
            } else {
                it
            }

        }
        _state.value = _state.value.copy(
            bikes = updatedBikeList
        )
    }


    private fun loadUserProfile() {
        viewModelScope.launch {

            val profile = profileRepository.getProfile()
            val bikes = bikeRepository.getBikes()

            profile?.let {
                _profileState.value = profile
                _state.value = _state.value.copy(
                    bikes = bikes,
                    status = SetupProfileScreenStatus.DisplayingProfile
                )
            }
        }
    }

    fun saveProfileChanges() {

        val firstName = _profileState.value?.firstname ?: return
        val lastName = _profileState.value?.lastname ?: return

        viewModelScope.launch {
            _state.value = _state.value.copy(
                status = SetupProfileScreenStatus.SavingProfile
            )
            try {

                _profileState.value?.let { profile ->
                    val r = profileRepository.updateProfile(profile)


                    when (r) {
                        is RepositoryResult.Success -> {
                            bikeRepository.updateSynchronizedBikes(
                                state.value.bikes.filter { !it.draft }.map { it._id }
                            )
                            updateEvent.emit(true)
                        }

                        else -> {
                            _state.value = _state.value.copy(
                                status = SetupProfileScreenStatus.DisplayingProfile
                            )
                            updateEvent.emit(false)
                        }
                    }


                }
            } catch (err: Exception) {
                Log.e("ProfileScreenViewModel", "Error", err)
                // do nothing
                updateEvent.emit(false)
            }
        }
    }

    fun updateProfileImage(imageByteArray: ByteArray?) {
        viewModelScope.launch {

            imageByteArray?.let {

                api.uploadImageToFirebase(dataStore.getAuthUserOrFail(), it,
                    onUpdateUpload = { updatePercent ->
                        _state.value = _state.value.copy(profileImagePercent = updatePercent)
                    },
                    onSuccess = { url ->
                        _state.value = _state.value.copy(
                            profileImagePercent = 0f,
                        )
                        _profileState.value = _profileState.value?.copy(
                            profilePhotoUrl = url,
                        )
                    },
                    onFailure = {

                    })
            }

        }
    }

}