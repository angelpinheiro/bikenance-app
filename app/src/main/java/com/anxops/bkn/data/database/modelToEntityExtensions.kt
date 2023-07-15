package com.anxops.bkn.data.database

import com.anxops.bkn.data.database.entities.BikeEntity
import com.anxops.bkn.data.database.entities.BikeRideEntity
import com.anxops.bkn.data.database.entities.ComponentEntity
import com.anxops.bkn.data.database.entities.ProfileEntity
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.Profile
import com.anxops.bkn.util.formatAsIso8061


fun Profile.toEntity(): ProfileEntity {
    return ProfileEntity(
        _id = _id,
        userId = userId,
        username = username,
        firstName = firstname,
        lastName = lastname,
        profilePhoto = profilePhotoUrl,
        createdAt = createdAt,
//        biggestRideDistance = biggestRideDistance,
//        biggestClimbElevationGain = biggestClimbElevationGain,
//        recentRideTotalDistance = recentRideTotalDistance,
//        recentRideTotalDuration = recentRideTotalDuration,
//        ytdRideTotalDistance = ytdRideTotalDistance,
//        ytdRideTotalDuration = ytdRideTotalDuration,
//        allRideTotalDistance = allRideTotalDistance,
//        allRideTotalDuration = allRideTotalDuration
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
        electric = electric,
        configDone = configDone,
        draft = draft,
        type = type.toString()
    )
}

fun BikeRide.toEntity(): BikeRideEntity {
    return BikeRideEntity(
        _id = _id,
        userId = userId,
        bikeId = bikeId,
        stravaId = stravaId,
        activityType = activityType,
        sportType = sportType,
        name = name,
        distance = distance,
        movingTime = movingTime,
        elapsedTime = elapsedTime,
        elevationGain = totalElevationGain,
        dateTime = dateTime?.formatAsIso8061(),
        mapSummaryPolyline = mapSummaryPolyline
    )
}

fun BikeComponent.toEntity(): ComponentEntity {
    return ComponentEntity(
        _id = _id ?: throw NullPointerException("ComponentEntity id can not be null"),
        bikeId = bikeId,
        description = alias,
        type = type.name,
        usageDistance = usage?.distance ?: 0.0,
        usageHours = usage?.duration ?: 0.0,
        from = from?.formatAsIso8061()
    )
}


