package com.anxops.bkn.data.network

import com.anxops.bkn.BuildConfig

object ApiEndpoints {
    private const val baseApiUrl = BuildConfig.API_URL

    const val stravaLoginEndpoint = "$baseApiUrl/strava"

    const val refreshTokenEndpoint = "$baseApiUrl/refresh"
    const val profileEndpoint = "$baseApiUrl/profile"
    const val profileBikesEndpoint = "$baseApiUrl/profile/bikes"
    const val profileRidesEndpoint = "$baseApiUrl/profile/rides"
    const val profileRidesByKeyEndpoint = "$baseApiUrl/profile/pagedRides"
    const val profileSyncBikesEndpoint = "$baseApiUrl/profile/sync"

    const val firebaseTokenEndpoint = "$baseApiUrl/users/messagingToken"

    fun extendedProfileEndpoint(draft: Boolean = false) =
        "$baseApiUrl/profile/extended?draft=$draft"

    fun profileBikeEndpoint(bikeId: String) = "$baseApiUrl/profile/bikes/$bikeId"
    fun profileRideEndpoint(rideId: String) = "$baseApiUrl/profile/rides/$rideId"
}