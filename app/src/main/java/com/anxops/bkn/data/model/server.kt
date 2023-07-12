package com.anxops.bkn.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = BikeTypeSerializer::class)
enum class BikeType(
    val type: String,
    val extendedType: String
) {
    UNKNOWN("UNKNOWN", "Unknown bike type"),
    MTB("MTB", "Mountain Bike"),
    FULL_MTB("Full-MTB", "Full Suspension Mountain Bike"),
    ROAD("Road", "Road Bike"),
    E_BIKE("E-Bike","Electric Bike"),
    GRAVEL("Gravel", "Gravel Bike"),
    STATIONARY("Stationary", "Stationary Bike")
}

@Serializable
data class ExtendedProfile(
    val profile: Profile?,
    val bikes: List<Bike>? = null,
    val rides: List<BikeRide>? = null
)

@Serializable
data class Profile(
    val _id: String,
    @SerialName("user_id") var userId: String,
    @SerialName("username") var username: String? = null,
    @SerialName("firstname") var firstname: String? = null,
    @SerialName("lastname") var lastname: String? = null,
    @SerialName("profile_photo_url") var profilePhotoUrl: String? = null,
    @SerialName("sex") var sex: String? = null,
    @SerialName("weight") var weight: Int? = null,
    @SerialName("created_at") var createdAt: String? = null,

    )

@Serializable
data class Bike(
    val _id: String,
    @SerialName("user_id") var userId: String? = null,
    @SerialName("strava_gear_id") var stravaId: String? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("brand_name") var brandName: String? = null,
    @SerialName("model_name") var modelName: String? = null,
    @SerialName("distance") var distance: Long? = null,
    @SerialName("photo_url") var photoUrl: String? = null,
    @SerialName("draft") var draft: Boolean = false,
    @SerialName("bike_type") var type: BikeType = BikeType.UNKNOWN,
) {
    fun km(): Long? {
        return distance?.div(1000)
    }

    fun toBikeUpdate(): BikeUpdate {
        return BikeUpdate(
            userId,
            stravaId,
            name,
            brandName,
            modelName,
            distance,
            photoUrl,
            draft,
            type
        )
    }

    fun displayName(): String {
        return name ?: (brandName ?: "") + " " + (modelName ?: "")
    }
}

@Serializable
data class BikeUpdate(
    @SerialName("user_id") var userId: String? = null,
    @SerialName("strava_gear_id") var stravaId: String? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("brand_name") var brandName: String? = null,
    @SerialName("model_name") var modelName: String? = null,
    @SerialName("distance") var distance: Long? = null,
    @SerialName("photo_url") var photoUrl: String? = null,
    @SerialName("draft") var draft: Boolean = false,
    @SerialName("bike_type") var type: BikeType = BikeType.UNKNOWN,
)

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
    @SerialName("date_time") var dateTime: String? = null,
    @SerialName("map_summary_polyline") var mapSummaryPolyline: String? = null,
)

