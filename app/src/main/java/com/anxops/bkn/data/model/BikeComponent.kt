package com.anxops.bkn.data.model

import com.anxops.bkn.data.model.util.LocalDateSerializer
import com.anxops.bkn.util.formatDistanceAsKm
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime


enum class ComponentModifier(val displayName: String) {
    REAR("Rear"), FRONT("Front")
}

@Serializable
data class BikeComponent(
    val _id: String? = null,
    @SerialName("bikeId") val bikeId: String?,
    @SerialName("alias") val alias: String? = null,
    @SerialName("type") val type: ComponentType,
    @SerialName("modifier") val modifier: ComponentModifier? = null,
    @SerialName("usage") var usage: Usage? = null,
    @Serializable(with = LocalDateSerializer::class) @SerialName("from") val from: LocalDateTime? = null,
    @SerialName("maintenances") val maintenances: List<Maintenance>? = null,

    ) {


    val status by lazy {
        maintenances?.maxByOrNull { it.status }?.status ?: 0.0
    }

    val statusLevel by lazy {
        StatusLevel.from(status)
    }

    fun displayDuration(): String {
        return "${usage?.duration?.let { (it / 3600).toInt() } ?: "--"} h"
    }

    fun displayDistance() = formatDistanceAsKm((usage?.distance ?: 0).toInt())

}