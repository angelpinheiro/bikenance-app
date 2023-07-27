package com.anxops.bkn.data.model

import com.anxops.bkn.data.model.util.ComponentTypeSerializer
import com.anxops.bkn.data.model.util.LocalDateSerializer
import com.anxops.bkn.data.model.util.MaintenanceTypeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

enum class RevisionUnit {
    KILOMETERS, HOURS, WEEKS, MONTHS, YEARS
}

@Serializable
data class RevisionFrequency(
    @SerialName("every") val every: Int, @SerialName("unit") val unit: RevisionUnit
) {
    fun displayText(): String {
        return "Every $every ${unit.name.lowercase()}"
    }
}

@Serializable
data class Maintenance(
    @SerialName("_id") val _id: String,
    @SerialName("componentId") val componentId: String,
    @Serializable(with = MaintenanceTypeSerializer::class) @SerialName("type") val type: MaintenanceType,
    @SerialName("defaultFrequency") val defaultFrequency: RevisionFrequency,
    @SerialName("description") val description: String,
    @Serializable(with = ComponentTypeSerializer::class) @SerialName("componentType") val componentType: ComponentType,
    @SerialName("usageSinceLast") var usageSinceLast: Usage?,
    @SerialName("status") val status: Double = 0.0,
    @Serializable(with = LocalDateSerializer::class) @SerialName("lastDate") var lastMaintenanceDate: LocalDateTime? = null,
    @Serializable(with = LocalDateSerializer::class) @SerialName("estimatedDate") var estimatedDate: LocalDateTime? = null,
) {
    fun displayStatus() = "${(status * 100).toInt()}%"

    fun statusLevel() = StatusLevel.from(status)
}