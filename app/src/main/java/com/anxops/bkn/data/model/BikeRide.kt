package com.anxops.bkn.data.model

import com.anxops.bkn.data.model.util.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class BikeRide(
    val _id: String,
    @SerialName("user_id") var userId: String? = null,
    @SerialName("bike_id") var bikeId: String? = null,
    @SerialName("strava_activity_id") var stravaId: String? = null,
    @SerialName("activity_type") var activityType: String? = null,
    @SerialName("sport_type") var sportType: String? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("distance") var distance: Int? = null,
    @SerialName("moving_time") var movingTime: Int? = null,
    @SerialName("elapsed_time") var elapsedTime: Int? = null,
    @SerialName("total_elevation_gain") var totalElevationGain: Int? = null,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("date_time") var dateTime: LocalDateTime? = null,
    @SerialName("map_summary_polyline") var mapSummaryPolyline: String? = null,
)