package com.anxops.bkn.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anxops.bkn.data.model.BikeRide
import kotlinx.serialization.SerialName


@Entity(tableName = "bike_ride")
data class BikeRideEntity(
    @PrimaryKey val _id: String,
    @ColumnInfo(name = "user_id") val userId: String?,
    @ColumnInfo(name = "bike_id") val bikeId: String?,
    @ColumnInfo(name = "strava_id") val stravaId: String?,
    @ColumnInfo(name = "activity_type") var activityType: String? = null,
    @ColumnInfo(name = "sport_type") var sportType: String? = null,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "distance") val distance: Int?,
    @ColumnInfo(name = "moving_time") val movingTime: Int?,
    @ColumnInfo(name = "elapsed_time") val elapsedTime: Int?,
    @ColumnInfo(name = "elevation") val elevationGain: Int?,
    @ColumnInfo(name = "date_time") val dateTime: String?,
    @ColumnInfo(name = "summary_polyline") val mapSummaryPolyline: String?,
) {
    fun toDomain(): BikeRide {
        return BikeRide(
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
            totalElevationGain = elevationGain,
            dateTime = dateTime,
            mapSummaryPolyline = mapSummaryPolyline
        )
    }
}

