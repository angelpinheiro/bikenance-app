package com.anxops.bkn.data.network

import com.anxops.bkn.BuildConfig

object ApiEndpoints {

    private var baseApiUrl = BuildConfig.API_URL + "/api"

    fun stravaLoginEndpoint() = "$baseApiUrl/strava"
    fun refreshTokenEndpoint() = "$baseApiUrl/refresh"
    fun profileEndpoint() = "$baseApiUrl/profile"
    fun profileBikesEndpoint() = "$baseApiUrl/profile/bikes"
    fun profileRidesEndpoint() = "$baseApiUrl/profile/rides"
    fun profileRidesByKeyEndpoint() = "$baseApiUrl/profile/pagedRides"
    fun profileSyncBikesEndpoint() = "$baseApiUrl/profile/sync"
    fun firebaseTokenEndpoint() = "$baseApiUrl/users/messagingToken"
    fun profileBikeEndpoint(bikeId: String) = "$baseApiUrl/profile/bikes/$bikeId"
    fun profileBikeSetupEndpoint(bikeId: String) = "$baseApiUrl/profile/bikes/$bikeId/setup"
    fun profileRideEndpoint(rideId: String) = "$baseApiUrl/profile/rides/$rideId"
    fun addComponentsEndpoint(bikeId: String) = "$baseApiUrl/profile/bikes/$bikeId/components"
    fun setUseDebugAPi(use: Boolean) {
        baseApiUrl = if (use) {
            BuildConfig.DEBUG_API_URL + "/api"
        } else {
            BuildConfig.API_URL + "/api"
        }
    }
}