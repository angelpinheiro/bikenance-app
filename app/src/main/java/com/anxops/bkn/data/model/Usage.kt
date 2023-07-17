package com.anxops.bkn.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Usage(
    @SerialName("duration")
    val duration: Double,
    @SerialName("distance")
    val distance: Double
)