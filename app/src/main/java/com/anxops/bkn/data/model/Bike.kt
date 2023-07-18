package com.anxops.bkn.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class BikeType(
    val type: String, val extendedType: String
) {
    MTB("MTB", "MTB Hardtail"), FULL_MTB("Full MTB", "MTB Full Suspension"), ROAD(
        "Road", "Road Bike"
    ),
    E_BIKE("E-Bike", "Electric Bike"), GRAVEL("Gravel", "Gravel Bike"), STATIONARY(
        "Stationary", "Stationary Bike"
    )
}

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

    fun status(): BikeStatus {
        val componentStatus = components?.map { it to it.status() } ?: listOf()

        val componentTypeStatus = componentStatus.groupBy {
            it.first.type
        }.map {
            it.key to it.value.maxBy { cs -> cs.second }.second
        }.toMap()

        val categoryStatus = componentStatus.groupBy {
            it.first.type.category
        }.map {
            it.key to it.value.maxBy { cs -> cs.second }.second
        }.toMap()

        val generalStatus = categoryStatus.maxBy { it.value }.value

        return BikeStatus(generalStatus,
            categoryStatus,
            componentTypeStatus,
            componentStatus.associate { it.first to it.second })

    }
}

enum class StatusLevel {

    GOOD, OK, WARN, DANGER;

    companion object {

        fun from(status: Double) = when {
            status >= 1 -> {
                DANGER
            }

            status > 0.7 -> {
                WARN
            }

//            status > 0.5 -> {
//                GOOD
//            }

            else -> {
                GOOD
            }
        }
    }
}

data class BikeStatus(
    val globalStatus: StatusLevel,
    val componentCategoryStatus: Map<ComponentCategory, StatusLevel>,
    val componentTypeStatus: Map<ComponentTypes, StatusLevel>,
    val componentStatus: Map<BikeComponent, StatusLevel>
)