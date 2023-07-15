package com.anxops.bkn.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date
import kotlin.math.round

enum class BikeType(
    val type: String,
    val extendedType: String
) {
    MTB("MTB", "MTB Hardtail"),
    FULL_MTB("Full MTB", "MTB Full Suspension" ),
    ROAD("Road", "Road Bike"),
    E_BIKE("E-Bike", "Electric Bike"),
    GRAVEL("Gravel", "Gravel Bike"),
    STATIONARY("Stationary", "Stationary Bike")
}

@Serializable
data class AthleteStats(
    @SerialName("biggest_ride_distance") val biggestRideDistance: Double,
    @SerialName("biggest_climb_elevation_gain") val biggestClimbElevationGain: Double,
    @SerialName("recent_ride_totals") val recentRideTotals: ActivityTotal,
    @SerialName("ytd_ride_totals") val ytdRideTotals: ActivityTotal,
    @SerialName("all_ride_totals") val allRideTotals: ActivityTotal,
) {
    fun recentAverageActivity(): ActivityTotal {
        return ActivityTotal(
            count = recentRideTotals.count,
            duration = recentRideTotals.duration / recentRideTotals.count,
            distance = recentRideTotals.distance / recentRideTotals.count,
            elevationGain = recentRideTotals.elevationGain / recentRideTotals.count,
            movingTime = recentRideTotals.movingTime / recentRideTotals.count
        )
    }

    fun totalsAverageActivity(): ActivityTotal {
        return ActivityTotal(
            count = allRideTotals.count,
            duration = allRideTotals.duration / allRideTotals.count,
            distance = allRideTotals.distance / allRideTotals.count,
            elevationGain = allRideTotals.elevationGain / allRideTotals.count,
            movingTime = allRideTotals.movingTime / allRideTotals.count
        )
    }

    fun ytdAverageActivity(): ActivityTotal {
        return ActivityTotal(
            count = ytdRideTotals.count,
            duration = ytdRideTotals.duration / ytdRideTotals.count,
            distance = ytdRideTotals.distance / ytdRideTotals.count,
            elevationGain = ytdRideTotals.elevationGain / ytdRideTotals.count,
            movingTime = ytdRideTotals.movingTime / ytdRideTotals.count
        )
    }

    fun ridesPerYear(): Double {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val daysFromStartOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        return 365f * ytdRideTotals.count / daysFromStartOfYear
    }

    fun monthlyEstimationBasedOnYTD(): Pair<Usage, ActivityTotal> {

        val ridesPerYear = 365f * ytdRideTotals.count / ridesPerYear()
        val ridesPerMonth = ridesPerYear / 12

        val avgDistance = ytdRideTotals.distance / ytdRideTotals.count
        val avgDuration = ytdRideTotals.duration / ytdRideTotals.count
        val avgElevation = ytdRideTotals.elevationGain / ytdRideTotals.count
        val avgMovingTime = ytdRideTotals.movingTime / ytdRideTotals.count

        val monthlyDistance = ridesPerMonth * avgDistance
        val monthlyDurationHours = ridesPerMonth * avgDuration / 3600

        return Usage(monthlyDurationHours, monthlyDistance) to ActivityTotal(
            count = ridesPerMonth,
            duration = avgDuration,
            distance = avgDistance,
            elevationGain = avgElevation,
            movingTime = avgMovingTime
        )
    }
}

@Serializable
data class ActivityTotal(
    @SerialName("count") val count: Double,
    @SerialName("distance") val distance: Double,
    @SerialName("elapsed_time") val duration: Double,
    @SerialName("moving_time") val movingTime: Double,
    @SerialName("elevation_gain") val elevationGain: Double,
) {
    fun hours(): Double {
        return round(duration / 3600 * 10) / 10
    }
}


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
    @SerialName("stats") var stats: AthleteStats? = null

//
//    @SerialName("biggest_ride_distance") val biggestRideDistance: Double = 0.0,
//    @SerialName("biggest_climb_elevation_gain") val biggestClimbElevationGain: Double = 0.0,
//    @SerialName("recent_ride_total_distance") val recentRideTotalDistance: Double = 0.0,
//    @SerialName("recent_ride_total_duration") val recentRideTotalDuration: Double = 0.0,
//    @SerialName("ytd_ride_total_distance") val ytdRideTotalDistance: Double = 0.0,
//    @SerialName("ytd_ride_totals_duration") val ytdRideTotalDuration: Double = 0.0,
//    @SerialName("all_ride_totals_distance") val allRideTotalDistance: Double = 0.0,
//    @SerialName("all_ride_total_duration") val allRideTotalDuration: Double = 0.0,

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
    @SerialName("electric") val electric: Boolean = false,
    @SerialName("configDone") val configDone: Boolean = false,
    @SerialName("bike_type") var type: BikeType = BikeType.MTB,

    val components: List<BikeComponent>? = emptyList()

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
            electric,
            configDone,
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
    @SerialName("electric") val electric: Boolean,
    @SerialName("configDone") val configDone: Boolean,
    @SerialName("bike_type") var type: BikeType = BikeType.MTB,
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
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("date_time") var dateTime: LocalDateTime? = null,
    @SerialName("map_summary_polyline") var mapSummaryPolyline: String? = null,
)

