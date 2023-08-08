package com.anxops.bkn.data.model

import com.anxops.bkn.data.model.util.ComponentTypeSerializer
import com.anxops.bkn.data.model.util.LocalDateSerializer
import com.anxops.bkn.data.model.util.MaintenanceTypeSerializer
import com.anxops.bkn.data.model.util.expectedNextMaintenanceDate
import com.anxops.bkn.data.model.util.wearPercentage
import java.time.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun revisionUnitRange(unit: RevisionUnit): IntRange {
    return when (unit) {
        RevisionUnit.HOURS -> IntRange(1, 1000)
        RevisionUnit.KILOMETERS -> IntRange(1, 10000)
        RevisionUnit.WEEKS -> IntRange(1, 12)
        RevisionUnit.MONTHS -> IntRange(1, 24)
        RevisionUnit.YEARS -> IntRange(1, 10)
    }
}

enum class RevisionUnit {
    KILOMETERS, HOURS, WEEKS, MONTHS, YEARS
}

@Serializable
data class RevisionFrequency(
    @SerialName("every") val every: Int,
    @SerialName("unit") val unit: RevisionUnit
) {
    fun displayText(): String {
        return "Every $every ${unit.name.lowercase()}"
    }
}

@Serializable
data class Maintenance(
    @SerialName("_id") val _id: String,
    @SerialName("componentId") val componentId: String,
    @Serializable(with = MaintenanceTypeSerializer::class)
    @SerialName("type")
    val type: MaintenanceType,
    @SerialName("defaultFrequency") val defaultFrequency: RevisionFrequency,
    @SerialName("description") val description: String,
    @Serializable(with = ComponentTypeSerializer::class)
    @SerialName("componentType")
    val componentType: ComponentType,
    @SerialName("usageSinceLast") var usageSinceLast: Usage = Usage(0.0, 0.0),
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("lastDate")
    val lastMaintenanceDate: LocalDateTime? = null
) {

    val status: Double by lazy {
        wearPercentage(LocalDateTime.now())
    }

    val estimatedDate: LocalDateTime? by lazy {
        expectedNextMaintenanceDate(LocalDateTime.now())
    }

    fun displayStatus() = "${(status * 100).toInt()}%"

    fun statusLevel() = StatusLevel.from(status)
}
