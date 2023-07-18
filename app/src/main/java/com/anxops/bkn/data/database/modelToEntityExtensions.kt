package com.anxops.bkn.data.database

import com.anxops.bkn.data.database.entities.BikeEntity
import com.anxops.bkn.data.database.entities.BikeRideEntity
import com.anxops.bkn.data.database.entities.BikeStatsEntity
import com.anxops.bkn.data.database.entities.ComponentEntity
import com.anxops.bkn.data.database.entities.MaintenanceEntity
import com.anxops.bkn.data.database.entities.ProfileEntity
import com.anxops.bkn.data.database.entities.UsageEntity
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.data.model.BikeStats
import com.anxops.bkn.data.model.Maintenance
import com.anxops.bkn.data.model.Profile
import com.anxops.bkn.data.model.Usage
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
        type = type.toString(),
        stats = stats?.toEntity()
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
        modifier = modifier?.name,
        from = from?.formatAsIso8061()
    )
}

fun Maintenance.toEntity(): MaintenanceEntity {
    return MaintenanceEntity(
        _id = _id,
        componentId = componentId,
        usageSinceLast = usageSinceLast?.toEntity(),
        estimatedDate = estimatedDate?.formatAsIso8061(),
        description = description,
        lastMaintenanceDate = lastMaintenanceDate?.formatAsIso8061(),
        componentType = componentType.name,
        type = type.name,
        status = status,
        defaultFrequencyEvery = defaultFrequency.every,
        defaultFrequencyUnit = defaultFrequency.unit.name,
    )
}

fun Usage.toEntity(): UsageEntity {
    return UsageEntity(duration, distance)
}

fun BikeStats.toEntity(): BikeStatsEntity {
    return BikeStatsEntity(
        rideCount = rideCount,
        elevationGain = elevationGain,
        distance = distance,
        duration = duration,
        averageSpeed = averageSpeed,
        maxSpeed = maxSpeed,
        lastRideDate = lastRideDate?.formatAsIso8061()
    )
}



