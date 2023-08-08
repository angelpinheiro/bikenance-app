package com.anxops.bkn.data.model

import androidx.room.ColumnInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Usage(
    @ColumnInfo(name = "usage_duration")
    @SerialName("duration")
    val duration: Double,

    @ColumnInfo(name = "usage_distance")
    @SerialName("distance")
    val distance: Double
)
