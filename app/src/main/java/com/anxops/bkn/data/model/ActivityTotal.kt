package com.anxops.bkn.data.model

import kotlin.math.round
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivityTotal(
    @SerialName("count") val count: Double,
    @SerialName("distance") val distance: Double,
    @SerialName("elapsed_time") val duration: Double,
    @SerialName("moving_time") val movingTime: Double,
    @SerialName("elevation_gain") val elevationGain: Double
) {
    fun hours(): Double {
        return round(duration / 3600 * 10) / 10
    }
}
