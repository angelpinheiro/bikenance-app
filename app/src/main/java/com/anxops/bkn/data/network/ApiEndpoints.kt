/*
 * Copyright 2023 Angel Piñeiro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    fun extendedProfileEndpoint(draft: Boolean = false) = "$baseApiUrl/profile/extended?draft=$draft"
    fun profileBikeEndpoint(bikeId: String) = "$baseApiUrl/profile/bikes/$bikeId"
    fun profileRideEndpoint(rideId: String) = "$baseApiUrl/profile/rides/$rideId"
}