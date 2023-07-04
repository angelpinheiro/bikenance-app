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

package com.anxops.bkn.data.database

import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.model.Profile
import com.anxops.bkn.data.database.entities.BikeEntity
import com.anxops.bkn.data.database.entities.BikeRideEntity
import com.anxops.bkn.data.database.entities.ProfileEntity


fun Profile.toEntity() : ProfileEntity {
    return ProfileEntity(
        _id = _id,
        userId = userId,
        username = username,
        firstName = firstname,
        lastName = lastname,
        profilePhoto = profilePhotoUrl,
        createdAt = createdAt
    )
}

fun Bike.toEntity() : BikeEntity {
    return BikeEntity(
        _id = _id,
        userId = userId,
        stravaId = stravaId,
        name = name,
        brandName = brandName,
        modelName = modelName,
        distance = distance,
        photoUrl = photoUrl,
        draft = draft
    )
}

fun BikeRide.toEntity() : BikeRideEntity {
    return BikeRideEntity(
        _id = _id,
        userId = userId,
        bikeId = bikeId,
        stravaId = stravaId,
        name = name,
        distance = distance,
        movingTime = movingTime,
        elapsedTime = elapsedTime,
        elevationGain = totalElevationGain,
        dateTime = dateTime,
        mapSummaryPolyline = mapSummaryPolyline
    )
}