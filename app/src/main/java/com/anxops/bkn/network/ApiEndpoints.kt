package com.anxops.bkn.network

object ApiEndpoints {
    private const val baseApiUrl = "https://bikenanceapp.loca.lt"

    const val profileEndpoint = "$baseApiUrl/profile"
    const val profileBikesEndpoint = "$baseApiUrl/profile/bikes"
    const val profileRidesEndpoint = "$baseApiUrl/profile/rides"
    const val profileSyncBikesEndpoint = "$baseApiUrl/profile/sync"
    const val uploadFileEndpoint = "$baseApiUrl/upload"
    const val filesEndpoint = "$baseApiUrl/files"

    const val firebaseTokenEndpoint = "$baseApiUrl/users/messagingToken"

    fun extendedProfileEndpoint(draft: Boolean = false) = "$baseApiUrl/profile/extended?draft=$draft"
    fun profileBikeEndpoint(bikeId: String) = "$baseApiUrl/profile/bikes/$bikeId"
    fun profileRideEndpoint(rideId: String) = "$baseApiUrl/profile/rides/$rideId"
}