package com.anxops.bkn.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.model.Bike
import com.anxops.bkn.model.BikeType
import com.anxops.bkn.network.Api
import com.anxops.bkn.network.ApiResponse
import com.anxops.bkn.storage.BikeRepositoryFacade
import com.anxops.bkn.storage.BknDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class NewBikeScreenStatus {
    object Loading : NewBikeScreenStatus()
    object Saving : NewBikeScreenStatus()
    object Displaying : NewBikeScreenStatus()
}

data class NewBikeScreenState(
    val bike: Bike = Bike(_id = ""),
    val status: NewBikeScreenStatus = NewBikeScreenStatus.Loading,
    val imageUploadPercent: Float = 0F,
//    val storageRef: StorageReference? = null
)

@HiltViewModel
class NewBikeScreenViewModel @Inject constructor(
    private val dataStore: BknDataStore,
    private val api: Api,
    private val repository: BikeRepositoryFacade,
) :
    ViewModel() {

    private var _state = MutableStateFlow(NewBikeScreenState())
    val updateEvent: MutableSharedFlow<Boolean> = MutableSharedFlow()

    val state: StateFlow<NewBikeScreenState> = _state

    fun isNewBike(): Flow<Boolean> = state.map { it.bike._id.isBlank() }

    init {
        if (state.value.bike._id.isNotBlank()) {
            loadBike(state.value.bike._id)
        } else {
            _state.value = _state.value.copy(status = NewBikeScreenStatus.Displaying)
        }
    }

    fun loadBike(bikeId: String) {

        if (_state.value.bike._id == bikeId)
            return

        viewModelScope.launch {

            when (val r = api.getBike(bikeId)) {
                is ApiResponse.Success -> {
                    _state.value = _state.value.copy(
                        status = NewBikeScreenStatus.Displaying,
                        bike = r.data,
//                        storageRef = r.data.photoUrl?.let { api.getStorageRef(it) }
                    )
                }
                else -> {
                    TODO("Handle api call failure")
                }
            }
        }
    }


    fun updateBikeImage(byteArray: ByteArray?) {

        viewModelScope.launch {

            byteArray?.let {
                api.uploadImageToFirebase(dataStore.getAuthUserOrFail(), it,
                    onUpdateUpload = { updatePercent ->
                        _state.value = _state.value.copy(imageUploadPercent = updatePercent)
                    },
                    onSuccess = { url ->
                        _state.value = _state.value.copy(
                            imageUploadPercent = 0f,
                            bike = _state.value.bike.copy(
                                photoUrl = url,
                            ),
//                            storageRef = api.getStorageRef(fileId)
                        )
                    },
                    onFailure = {

                    })
            }

        }

//        inputStream?.readBytes()?.let {
//            updateBikeImage(it)
//        }
    }

//    fun updateBikeImage(imageByteArray: ByteArray?) {
//        viewModelScope.launch {
//            dataStore.getAuthToken()?.let { token ->
//                api.uploadImage(imageByteArray, token) { updatePercent ->
//                    _state.value = _state.value.copy(imageUploadPercent = updatePercent)
//                }?.let { fileId ->
//                    _state.value = _state.value.copy(
//                        imageUploadPercent = 0f,
//                        bike = _state.value.bike.copy(
//                            photoUrl = api.fileUri(fileId),
//                        )
//                    )
//                }
//            }
//        }
//    }

    fun updateName(value: String) {
        _state.value = _state.value.copy(
            bike = _state.value.bike.copy(
                name = value
            )
        )
    }

    fun updateBrandName(value: String) {
        _state.value = _state.value.copy(
            bike = _state.value.bike.copy(
                brandName = value
            )
        )
    }

    fun updateModel(value: String) {
        _state.value = _state.value.copy(
            bike = _state.value.bike.copy(
                modelName = value
            )
        )
    }

    fun updateDistance(km: Long?) {
        _state.value = _state.value.copy(
            bike = _state.value.bike.copy(
                distance = km?.times(1000),
            )
        )
    }

    fun updateBikeType(type: BikeType) {
        _state.value = _state.value.copy(
            bike = _state.value.bike.copy(
                type = type
            )
        )
    }

    fun onSaveBike() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                status = NewBikeScreenStatus.Saving
            )
            try {
                if (state.value.bike._id.isNotBlank()) {
                    repository.updateBike(_state.value.bike)
                } else {
                    repository.createBike(_state.value.bike)
                }

                updateEvent.emit(true)
            } catch (err: Exception) {
                Log.e("ProfileScreenViewModel", "Error", err)
                // do nothing
                updateEvent.emit(false)
            }
        }
    }

    fun deleteBike() {
        viewModelScope.launch {
            dataStore.getAuthToken()?.let { token ->
                _state.value = _state.value.copy(
                    status = NewBikeScreenStatus.Saving
                )

                try {

                    if (state.value.bike._id.isNotBlank()) {
                        repository.deleteBike(_state.value.bike)
                    }
                    updateEvent.emit(true)
                } catch (err: Exception) {
                    Log.e("ProfileScreenViewModel", "Error", err)
                    // do nothing
                    updateEvent.emit(false)
                }
            }
        }

    }




}