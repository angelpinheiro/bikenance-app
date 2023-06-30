package com.anxops.bkn.network
import com.anxops.bkn.model.Bike
import com.anxops.bkn.model.BikeRide
import com.anxops.bkn.model.ExtendedProfile
import com.anxops.bkn.model.Profile
import com.anxops.bkn.storage.BknDataStore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*


class Api(client: KtorClient, val dataStore: BknDataStore) {

    private val fileClient = client.fileClient
    private val httpClient = client.client

    private suspend fun tokenHeader(): String {
        val token = dataStore.getAuthToken() ?: throw IllegalStateException("User token not found")
        return "Bearer $token"
    }

    suspend fun profile(): ApiResponse<Profile> {
        return safeApiCall {
            httpClient.get(ApiEndpoints.profileEndpoint) {
                header("Authorization", tokenHeader())
            }
        }
    }

    suspend fun extendedProfile(includeDraftBikes: Boolean = false): ApiResponse<ExtendedProfile> {
        return safeApiCall {
            httpClient.get(ApiEndpoints.extendedProfileEndpoint(includeDraftBikes)) {
                header("Authorization", tokenHeader())
            }
        }
    }

    suspend fun getBike(bikeId: String): ApiResponse<Bike> {
        return safeApiCall {
            httpClient.get(ApiEndpoints.profileBikeEndpoint(bikeId)) {
                header("Authorization", tokenHeader())
            }
        }
    }

    suspend fun getBikes(includeDraftBikes: Boolean = false): ApiResponse<List<Bike>> {
        return safeApiCall {
            httpClient.get(ApiEndpoints.profileBikesEndpoint) {
                header("Authorization", tokenHeader())
            }
        }
    }

    suspend fun getRides(): ApiResponse<List<BikeRide>> {
        return safeApiCall {
            httpClient.get(ApiEndpoints.profileRidesEndpoint) {
                header("Authorization", tokenHeader())
            }
        }
    }

    suspend fun updateProfile(update: Profile): ApiResponse<Profile> {
        return safeApiCall {
            httpClient.put(ApiEndpoints.profileEndpoint) {
                header("Authorization", tokenHeader())
                body = update
            }
        }
    }

    suspend fun updateBike(bike: Bike) : ApiResponse<Bike>{
        return safeApiCall {
            httpClient.put(ApiEndpoints.profileBikeEndpoint(bike._id)) {
                header("Authorization", tokenHeader())
                body = bike.toBikeUpdate()
            }
        }
    }

    suspend fun updateRide(ride: BikeRide): ApiResponse<BikeRide> {
        return safeApiCall {
            httpClient.put(ApiEndpoints.profileRideEndpoint(ride._id)) {
                header("Authorization", tokenHeader())
                body = ride
            }
        }
    }

    suspend fun createBike(bike: Bike) : ApiResponse<Bike>{
        return safeApiCall {
            httpClient.post(ApiEndpoints.profileBikesEndpoint) {
                header("Authorization", tokenHeader())
                body = bike.toBikeUpdate()
            }
        }
    }

    suspend fun deleteBike(bike: Bike) : ApiResponse<String>{
        return safeApiCall {
            httpClient.delete(ApiEndpoints.profileBikeEndpoint(bike._id)) {
                header("Authorization", tokenHeader())
            }
        }
    }

    suspend fun syncBikes(ids: List<String>)  : ApiResponse<Boolean>{
        return safeApiCall {
            httpClient.put(ApiEndpoints.profileSyncBikesEndpoint) {
                header("Authorization", tokenHeader())
                body = SyncBikes(ids)
            }
        }
    }

    suspend fun uploadImageToFirebase(
        userId: String,
        byteArray: ByteArray,
        onUpdateUpload: (Float) -> Unit = {},
        onSuccess: (String) -> Unit = {},
        onFailure: () -> Unit = {}
    ) {
        try {

            val imageId = UUID.randomUUID().toString() + ".jpg"
            var storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference
            val fileRef = storageRef.child(userId).child(imageId)
            val uploadTask = fileRef.putBytes(byteArray)

            uploadTask.addOnProgressListener { (bytesTransferred, totalByteCount) ->
                val progress = (100.0 * bytesTransferred) / totalByteCount
                onUpdateUpload(progress.toFloat())
            }.addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    fileRef.downloadUrl.addOnCompleteListener { urlTask ->
                        val url = urlTask.result
                        if(urlTask.isSuccessful && url != null) {
                            onSuccess(url.toString())
                        }else{
                            onFailure()
                        }
                    }
                }else{
                    onFailure()
                }
            }


        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    suspend fun updateFirebaseToken(token: String): Boolean {
        return httpClient.put(ApiEndpoints.firebaseTokenEndpoint) {
            header("Authorization", tokenHeader())
            body = TokenWrapper(token)
        }

    }

    suspend fun updateRefreshToken(refreshToken: String): ApiResponse<LoginResult> {
        return safeApiCall {
            httpClient.post(ApiEndpoints.refreshTokenEndpoint) {
                body = RefreshData(refreshToken)
            }
        }

    }


}

@Serializable
data class TokenWrapper(
    val token: String
)
@Serializable
data class RefreshData(
    val refreshToken: String,
)

@Serializable
data class LoginResult(
    val success: Boolean,
    val token: String? = null,
    val refreshToken: String? = null,
    val message: String? = null
)

@Serializable
data class SyncBikes(
    @SerialName("synchronizedBikesIds") val synchronizedBikesIds: List<String>
)