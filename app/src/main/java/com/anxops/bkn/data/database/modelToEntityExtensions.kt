package com.anxops.bkn.data.database

import com.anxops.bkn.data.database.entities.BikeEntity
import com.anxops.bkn.data.database.entities.BikeRideEntity
import com.anxops.bkn.data.database.entities.ComponentEntity
import com.anxops.bkn.data.database.entities.ProfileEntity
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.Profile


fun Profile.toEntity(): ProfileEntity {
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

fun Bike.toEntity(): BikeEntity {
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

fun BikeRide.toEntity(): BikeRideEntity {
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

fun BikeComponent.toEntity(): ComponentEntity {
    return ComponentEntity(
        _id = _id,
        bikeId = bikeId,
        description = description,
        componentType = type.type,
        componentTypeName = type.name,
        usageDistance = usage.km,
        usageHours = usage.hours
    )
}


