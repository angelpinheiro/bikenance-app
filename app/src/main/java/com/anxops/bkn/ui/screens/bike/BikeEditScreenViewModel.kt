package com.anxops.bkn.ui.screens.bike

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeType
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.ApiResponse
import com.anxops.bkn.data.network.ImageUploadRequest
import com.anxops.bkn.data.network.ImageUploader
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.repository.AppError
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.ErrorType
import com.anxops.bkn.data.repository.toAppError
import com.anxops.bkn.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


sealed class BikeEditScreenStatus {
    object Loading : BikeEditScreenStatus()
    object Saving : BikeEditScreenStatus()
    object Editing : BikeEditScreenStatus()
    object LoadFailed : BikeEditScreenStatus()
    object UpdateSuccess : BikeEditScreenStatus()
}

data class BikeEditScreenState(
    val bike: Bike = Bike(_id = ""),
    val status: BikeEditScreenStatus = BikeEditScreenStatus.Loading,
    val imageLoadProgress: Float = 0f,
    val error: AppError? = null
)

@HiltViewModel
class BikeEditScreenViewModel @Inject constructor(
    private val dataStore: BknDataStore,
    private val api: Api,
    private val repository: BikeRepositoryFacade,
    private val imageUploader: ImageUploader
) : ViewModel() {

    private val bikeFlow = MutableStateFlow(Bike(_id = ""))
    private val statusFlow = MutableStateFlow<BikeEditScreenStatus>(BikeEditScreenStatus.Loading)
    private val loadProgressFlow = MutableStateFlow(0.0f)
    private val errorStateFlow = MutableStateFlow<AppError?>(null)

    val state: StateFlow<BikeEditScreenState> = combine(
        bikeFlow,
        statusFlow,
        loadProgressFlow,
        errorStateFlow
    ) { bike, status, progress, error ->
        BikeEditScreenState(
            bike = bike, status = status, imageLoadProgress = progress, error = error
        )
    }.stateIn(viewModelScope, WhileUiSubscribed, BikeEditScreenState())


    fun loadBike(bikeId: String) {
        viewModelScope.launch {
            when (val r = api.getBike(bikeId)) {
                is ApiResponse.Success -> {
                    bikeFlow.update { r.data }
                    statusFlow.update { BikeEditScreenStatus.Editing }
                }
                is ApiResponse.Error -> {
                    statusFlow.update { BikeEditScreenStatus.LoadFailed }
                    errorStateFlow.update { r.exception.toAppError("Could not load bike") }
                }
            }
        }
    }


    fun updateBikeImage(byteArray: ByteArray?) {
        viewModelScope.launch {
            byteArray?.let { bytes ->
                imageUploader.uploadImage(ImageUploadRequest(
                    dataStore.getAuthUserOrFail(), bytes
                ), onProgressUpdate = { updatePercent ->
                    loadProgressFlow.update { updatePercent }
                }, onSuccess = { url ->
                    loadProgressFlow.update { 0.0f }
                    bikeFlow.update { it.copy(photoUrl = url) }
                }, onFailure = {
                    loadProgressFlow.update { 0.0f }
                    statusFlow.update { BikeEditScreenStatus.Editing }
                    errorStateFlow.update { AppError(ErrorType.Unexpected) }
                })
            }

        }
    }

    fun updateName(value: String) {
        bikeFlow.update { it.copy(name = value) }
    }

    fun updateBrandName(value: String) {
        bikeFlow.update { it.copy(brandName = value) }
    }

    fun updateModel(value: String) {
        bikeFlow.update { it.copy(modelName = value) }
    }

    fun updateBikeType(type: BikeType) {
        bikeFlow.update { it.copy(type = type) }
    }

    fun onSaveBike() {
        viewModelScope.launch {

            try {
                statusFlow.emit(BikeEditScreenStatus.Saving)
                if (bikeFlow.value._id.isNotBlank()) {
                    repository.updateBike(bikeFlow.value)
                } else {
                    repository.createBike(bikeFlow.value)
                }
                statusFlow.emit(BikeEditScreenStatus.UpdateSuccess)
            } catch (err: Exception) {
                Timber.e(err)
                statusFlow.emit(BikeEditScreenStatus.Editing)
                errorStateFlow.update { AppError(ErrorType.Unexpected) } // TODO
            }
        }
    }

    fun deleteBike() {
        viewModelScope.launch {
            dataStore.getAuthToken()?.let { token ->
                try {
                    statusFlow.emit(BikeEditScreenStatus.Saving)
                    if (bikeFlow.value._id.isNotBlank()) {
                        repository.deleteBike(bikeFlow.value)
                    }
                    statusFlow.emit(BikeEditScreenStatus.UpdateSuccess)
                } catch (err: Exception) {
                    Timber.e(err)
                    statusFlow.emit(BikeEditScreenStatus.Editing)
                    errorStateFlow.update { AppError(ErrorType.Unexpected) } // TODO
                }
            }
        }

    }

    fun onDismissError() {
        errorStateFlow.update { null }
    }


}