package com.anxops.bkn.data.model

import com.anxops.bkn.data.model.util.BikeTypeSerializer
import com.anxops.bkn.data.model.util.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

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
    @SerialName("full_suspension") val fullSuspension: Boolean = false,
    @SerialName("configDone") val configDone: Boolean = false,
    @Serializable(with = BikeTypeSerializer::class)
    @SerialName("bike_type") var type: BikeType = BikeType.Unknown,
    @SerialName("stats") var stats: BikeStats? = null,
    val components: List<BikeComponent> = emptyList()

) {

    val status by lazy {
        buildStatus()
    }

    fun km(): Long? {
        return distance?.div(1000)
    }

    fun toBikeUpdate(): BikeUpdate {
        return BikeUpdate(
            name,
            brandName,
            modelName,
            photoUrl,
            draft,
            electric,
            fullSuspension,
            configDone,
            type
        )
    }

    fun displayName(): String {
        return name ?: (brandName ?: "") + " " + (modelName ?: "")
    }

    fun fullDisplayName(): String {
        return (brandName ?: "") + " " + (modelName ?: "") + " (${type.extendedType})"
    }

    private fun buildStatus(): BikeStatus {
        val componentStatus = components?.map { it to it.statusLevel } ?: listOf()

        val componentTypeStatus = componentStatus.groupBy {
            it.first.type
        }.map {
            it.key to (it.value.maxByOrNull { cs -> cs.second }?.second ?: StatusLevel.UNKNOWN)
        }.toMap()

        val categoryStatus = componentStatus.groupBy {
            it.first.type.category
        }.map {
            it.key to (it.value.maxByOrNull { cs -> cs.second }?.second ?: StatusLevel.UNKNOWN)
        }.toMap()

        val generalStatus = categoryStatus.maxByOrNull { it.value }?.value ?: StatusLevel.UNKNOWN

        return BikeStatus(generalStatus,
            categoryStatus,
            componentTypeStatus,
            componentStatus.associate { it.first to it.second })

    }
}

enum class StatusLevel {

    UNKNOWN, GOOD, OK, WARN, DANGER;

    companion object {

        fun from(status: Double) = when {
            status >= 1 -> {
                DANGER
            }

            status > 0.7 -> {
                WARN
            }

            status in 0.0..0.7 -> {
                GOOD
            }

            else -> {
                UNKNOWN
            }
        }
    }
}

data class BikeStatus(
    val globalStatus: StatusLevel,
    val componentCategoryStatus: Map<ComponentCategory, StatusLevel>,
    val componentTypeStatus: Map<ComponentType, StatusLevel>,
    val componentStatus: Map<BikeComponent, StatusLevel>
)


@Serializable
data class BikeStats(
    @SerialName("ride_count")
    val rideCount: Double? = null,
    @SerialName("duration")
    val duration: Double = 0.0,
    @SerialName("distance")
    val distance: Double = 0.0,
    @SerialName("elevationGain")
    val elevationGain: Double = 0.0,
    @SerialName("average_speed")
    val averageSpeed: Double? = null,
    @SerialName("max_speed")
    val maxSpeed: Double? = null,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("last_ride_date")
    val lastRideDate: LocalDateTime? = null,
)



@Serializable
data class BikeUpdate(
    @SerialName("name") var name: String? = null,
    @SerialName("brand_name") var brandName: String? = null,
    @SerialName("model_name") var modelName: String? = null,
    @SerialName("photo_url") var photoUrl: String? = null,
    @SerialName("draft") var draft: Boolean = false,
    @SerialName("electric") val electric: Boolean = false,
    @SerialName("full_suspension") val fullSuspension: Boolean = false,
    @SerialName("configDone") val configDone: Boolean = false,
    @Serializable(with = BikeTypeSerializer::class)
    @SerialName("bike_type") var type: BikeType = BikeType.Unknown
)
