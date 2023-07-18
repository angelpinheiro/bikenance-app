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
import com.anxops.bkn.util.mutableStateIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
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
    val profileImagePercent: Float = 0f,
    val profile: Profile? = null,
)

@HiltViewModel
class SetupProfileScreenViewModel @Inject constructor(
    private val api: Api,
    private val dataStore: BknDataStore,
    private val profileRepository: ProfileRepositoryFacade,
    private val bikeRepository: BikeRepositoryFacade
) : ViewModel() {

    val updateEvent: MutableSharedFlow<Boolean> = MutableSharedFlow()


    private val bikesFlow = bikeRepository.getBikesFlow(true).filterNotNull()
    private val profileFlow = profileRepository.getProfileFlow().filterNotNull()

    private val _state: MutableStateFlow<SetupProfileScreenState> =
        bikesFlow.combine(profileFlow, transform = { bikes, profile ->
            SetupProfileScreenState(
                bikes = bikes.sortedByDescending { it.distance },
                profile = profile,
                status = if (state.value.status != SetupProfileScreenStatus.SavingProfile) {
                    SetupProfileScreenStatus.DisplayingProfile
                } else {
                    state.value.status
                }
            )
        }).mutableStateIn(viewModelScope, SetupProfileScreenState())

    val state = _state

    init {
        loadUserProfile()
    }


    fun updateFirstname(value: String) {
        _state.value = _state.value.copy(
            profile = _state.value.profile?.copy(firstname = value)
        )
    }

    fun updateLastname(value: String) {
        _state.value = _state.value.copy(
            profile = _state.value.profile?.copy(lastname = value)
        )
    }

    fun syncBike(id: String) {
        viewModelScope.launch {
            val updatedBikeList = _state.value.bikes.map {
                if (it._id == id) {
                    it.copy(draft = !it.draft)
                } else {
                    it
                }
            }

            _state.value =
                _state.value.copy(bikes = updatedBikeList.sortedByDescending { it.distance })

            Log.d("syncBike",
                "Bikes (${_state.value.bikes.size})" + _state.value.bikes.joinToString { "${it.name}: ${it.draft}" })
        }
    }


    private fun loadUserProfile() {
        viewModelScope.launch {
            bikeRepository.refreshBikes()
//            val profile = profileRepository.getProfile()
//            val bikes = bikeRepository.getBikes()
//
//            profile?.let {
//                _profileState.value = profile
//                _state.value = _state.value.copy(
//                    bikes = bikes,
//                    status = SetupProfileScreenStatus.DisplayingProfile
//                )
//            }
        }
    }

    fun saveProfileChanges() {

        val firstName = _state.value.profile?.firstname ?: return
        val lastName = _state.value.profile?.lastname ?: return

        viewModelScope.launch {
            _state.value = _state.value.copy(
                status = SetupProfileScreenStatus.SavingProfile
            )
            try {

                _state.value.profile?.let { profile ->
                    val r = profileRepository.updateProfile(profile)


                    when (r) {
                        is RepositoryResult.Success -> {
                            bikeRepository.updateSynchronizedBikes(state.value.bikes.associate { it._id to !it.draft })
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

                api.uploadImageToFirebase(dataStore.getAuthUserOrFail(),
                    it,
                    onUpdateUpload = { updatePercent ->
                        _state.value = _state.value.copy(profileImagePercent = updatePercent)
                    },
                    onSuccess = { url ->
                        _state.value = _state.value.copy(
                            profileImagePercent = 0f, profile = _state.value.profile?.copy(
                                profilePhotoUrl = url,
                            )
                        )
                    },
                    onFailure = {

                    })
            }

        }
    }

}