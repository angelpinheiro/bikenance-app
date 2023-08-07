package com.anxops.bkn.data.network

import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.model.Maintenance
import com.anxops.bkn.data.model.Profile
import com.anxops.bkn.data.preferences.BknDataStore
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*


class Api(client: KtorClient, val dataStore: BknDataStore) {

    private val httpClient = client.client

    private suspend fun tokenHeader(): String {
        val token = dataStore.getAuthToken() ?: throw IllegalStateException("User token not found")
        return "Bearer $token"
    }

    suspend fun profile(): ApiResponse<Profile> {
        return apiResponse {
            httpClient.get(ApiEndpoints.profileEndpoint()) {
                header("Authorization", tokenHeader())
            }
        }
    }

    suspend fun getBike(bikeId: String): ApiResponse<Bike> = apiResponse {
        httpClient.get(ApiEndpoints.profileBikeEndpoint(bikeId)) {
            header("Authorization", tokenHeader())
        }
    }

    suspend fun getBikes(): ApiResponse<List<Bike>> = apiResponse {
        httpClient.get(ApiEndpoints.profileBikesEndpoint()) {
            header("Authorization", tokenHeader())
        }
    }

    suspend fun getRides(): ApiResponse<List<BikeRide>> = apiResponse {
        httpClient.get(ApiEndpoints.profileRidesEndpoint()) {
            header("Authorization", tokenHeader())
        }
    }

    suspend fun refreshLastRides(): ApiResponse<Boolean> = apiResponse {
        httpClient.get(ApiEndpoints.profileRefreshLastRidesEndpoint()) {
            header("Authorization", tokenHeader())
        }
    }

    suspend fun getPaginatedRidesByDateTime(
        key: String?, pageSize: Int = 10
    ): ApiResponse<List<BikeRide>> = apiResponse {
        httpClient.get(ApiEndpoints.profileRidesByKeyEndpoint()) {
            header("Authorization", tokenHeader())
            key?.let {
                parameter("key", it)
            }
            parameter("pageSize", pageSize)
        }
    }

    suspend fun updateProfile(update: Profile): ApiResponse<Profile> = apiResponse {
        httpClient.put(ApiEndpoints.profileEndpoint()) {
            header("Authorization", tokenHeader())
            body = update
        }
    }

    suspend fun updateBike(bike: Bike): ApiResponse<Bike> = apiResponse {
        httpClient.put(ApiEndpoints.profileBikeEndpoint(bike._id)) {
            header("Authorization", tokenHeader())
            body = bike.toBikeUpdate()
        }
    }

    suspend fun updateRide(ride: BikeRide): ApiResponse<BikeRide> = apiResponse {
        httpClient.put(ApiEndpoints.profileRideEndpoint(ride._id)) {
            header("Authorization", tokenHeader())
            body = ride.toRideUpdate()
        }
    }

    suspend fun createBike(bike: Bike): ApiResponse<Bike> = apiResponse {
        httpClient.post(ApiEndpoints.profileBikesEndpoint()) {
            header("Authorization", tokenHeader())
            body = bike.toBikeUpdate()
        }
    }

    suspend fun deleteBike(bike: Bike): ApiResponse<String> = apiResponse {
        httpClient.delete(ApiEndpoints.profileBikeEndpoint(bike._id)) {
            header("Authorization", tokenHeader())
        }
    }

    suspend fun syncBikes(ids: Map<String, Boolean>): ApiResponse<Boolean> = apiResponse {
        httpClient.put(ApiEndpoints.profileSyncBikesEndpoint()) {
            header("Authorization", tokenHeader())
            body = SyncBikes(ids)
        }
    }

    suspend fun updateFirebaseToken(token: String): ApiResponse<Boolean> = apiResponse {
        httpClient.put(ApiEndpoints.firebaseTokenEndpoint()) {
            header("Authorization", tokenHeader())
            body = TokenWrapper(token)
        }

    }

    suspend fun updateRefreshToken(refreshToken: String): ApiResponse<LoginResult> = apiResponse {
        httpClient.post(ApiEndpoints.refreshTokenEndpoint()) {
            body = RefreshData(refreshToken)
        }
    }

    suspend fun setupBike(bike: Bike): ApiResponse<Bike> = apiResponse {
        httpClient.put(ApiEndpoints.profileBikeSetupEndpoint(bike._id)) {
            header("Authorization", tokenHeader())
            body = bike
        }
    }

    suspend fun updateMaintenance(bike: Bike, m: Maintenance): ApiResponse<Bike> = apiResponse {
        httpClient.put(
            ApiEndpoints.maintenanceEndpoint(
                bikeId = bike._id, maintenanceId = m._id
            )
        ) {
            header("Authorization", tokenHeader())
            body = m
        }
    }

    suspend fun replaceComponent(c: BikeComponent): ApiResponse<String> = apiResponse {
        httpClient.put(
            ApiEndpoints.replaceComponentEndpoint(
                bikeId = c.bikeId!!, componentId = c._id
            )
        ) {
            header("Authorization", tokenHeader())
            body = c
        }
    }

    /*suspend fun addBikeComponents(
        bikeId: String, newComponents: List<BikeComponent>
    ): ApiResponse<List<BikeComponent>> = apiResponse {
        httpClient.post(ApiEndpoints.addComponentsEndpoint(bikeId)) {
            header("Authorization", tokenHeader())
            body = newComponents
        }
    }*/

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
    @SerialName("syncData") val syncData: Map<String, Boolean>
)