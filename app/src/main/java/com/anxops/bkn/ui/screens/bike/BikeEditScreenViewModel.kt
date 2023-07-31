package com.anxops.bkn.ui.screens.bike

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeType
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.ApiResponse
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class BikeEditScreenStatus {
    object Loading : BikeEditScreenStatus()
    object Saving : BikeEditScreenStatus()
    object Editing : BikeEditScreenStatus()
    object Error : BikeEditScreenStatus()
    object UpdateSuccess : BikeEditScreenStatus()
}

data class NewBikeScreenState(
    val bike: Bike = Bike(_id = ""),
    val status: BikeEditScreenStatus = BikeEditScreenStatus.Loading,
    val imageLoadProgress: Float = 0f,
)

@HiltViewModel
class BikeEditScreenViewModel @Inject constructor(
    private val dataStore: BknDataStore,
    private val api: Api,
    private val repository: BikeRepositoryFacade,
) : ViewModel() {

    private val bikeFlow = MutableStateFlow(Bike(_id = ""))
    private val statusFlow = MutableStateFlow<BikeEditScreenStatus>(BikeEditScreenStatus.Loading)
    private val loadProgressFlow = MutableStateFlow(0.0f)

    val state: StateFlow<NewBikeScreenState> =
        combine(bikeFlow, statusFlow, loadProgressFlow) { bike, status, progress ->
            NewBikeScreenState(
                bike = bike, status = status, imageLoadProgress = progress
            )
        }.stateIn(viewModelScope, WhileUiSubscribed, NewBikeScreenState())


    fun loadBike(bikeId: String) {
        viewModelScope.launch {
            when (val r = api.getBike(bikeId)) {
                is ApiResponse.Success -> {
                    bikeFlow.update { r.data }
                    statusFlow.update { BikeEditScreenStatus.Editing }
                }

                else -> {
                    statusFlow.update { BikeEditScreenStatus.Error }
                }
            }
        }
    }


    fun updateBikeImage(byteArray: ByteArray?) {

        viewModelScope.launch {

            byteArray?.let { bytes ->
                api.uploadImageToFirebase(dataStore.getAuthUserOrFail(),
                    bytes,
                    onUpdateUpload = { updatePercent ->
                        loadProgressFlow.update { updatePercent }
                    },
                    onSuccess = { url ->
                        loadProgressFlow.update { 0.0f }
                        bikeFlow.update { it.copy(photoUrl = url) }
                    },
                    onFailure = {
                        statusFlow.update { BikeEditScreenStatus.Error }
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
                Log.e("ProfileScreenViewModel", "Error", err)
                statusFlow.emit(BikeEditScreenStatus.Error)
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
                    Log.e("ProfileScreenViewModel", "Error", err)
                    statusFlow.emit(BikeEditScreenStatus.Error)
                }
            }
        }

    }


}