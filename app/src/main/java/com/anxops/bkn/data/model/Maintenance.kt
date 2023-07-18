package com.anxops.bkn.data.model

import com.anxops.bkn.data.model.util.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

enum class RevisionUnit {
    KILOMETERS,
    HOURS,
    WEEKS,
    MONTHS,
    YEARS
}

@Serializable
data class RevisionFrequency(
    @SerialName("every")
    val every: Int,
    @SerialName("unit")
    val unit: RevisionUnit
)

@Serializable
data class Maintenance(
    @SerialName("_id")
    val _id: String,
    @SerialName("componentId")
    val componentId: String,
    @SerialName("type")
    val type: MaintenanceTypes,
    @SerialName("defaultFrequency")
    val defaultFrequency: RevisionFrequency,
    @SerialName("description")
    val description: String,
    @SerialName("componentType")
    val componentType: ComponentTypes,
    @SerialName("usageSinceLast")
    var usageSinceLast: Usage?,
    @SerialName("status")
    val status: Double = 0.0,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("lastDate")
    var lastMaintenanceDate: LocalDateTime? = null,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("estimatedDate")
    var estimatedDate: LocalDateTime? = null,
)