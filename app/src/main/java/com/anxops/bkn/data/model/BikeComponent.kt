package com.anxops.bkn.data.model

import com.anxops.bkn.data.model.util.LocalDateSerializer
import com.anxops.bkn.util.formatDistanceAsKm
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime


enum class ComponentModifier(val displayName: String) {
    REAR("Rear"),
    FRONT("Front")
}

@Serializable
data class BikeComponent(
    val _id: String? = null,
    @SerialName("bikeId")
    val bikeId: String?,
    @SerialName("alias")
    val alias: String? = null,
    @SerialName("type")
    val type: ComponentTypes,
    @SerialName("modifier")
    val modifier: ComponentModifier? = null,
    @SerialName("usage")
    var usage: Usage? = null,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("from")
    val from: LocalDateTime? = null,
) {
    fun displayDuration(): String {
        return "${usage?.duration?.let { (it / 3600).toInt() } ?: "--"} hours"
    }

    fun displayDistance() = formatDistanceAsKm((usage?.distance ?: 0).toInt())
}