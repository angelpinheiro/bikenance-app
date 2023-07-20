package com.anxops.bkn.data.model

import com.anxops.bkn.data.model.util.BikeTypeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


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
    @Serializable(with = BikeTypeSerializer::class)
    @SerialName("bike_type") var type: BikeType = BikeType.Unknown,
)


