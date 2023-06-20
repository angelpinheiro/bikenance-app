package com.anxops.bkn.storage.room

import com.anxops.bkn.model.Bike
import com.anxops.bkn.model.BikeRide
import com.anxops.bkn.model.Profile
import com.anxops.bkn.storage.room.entities.BikeEntity
import com.anxops.bkn.storage.room.entities.BikeRideEntity
import com.anxops.bkn.storage.room.entities.ProfileEntity


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