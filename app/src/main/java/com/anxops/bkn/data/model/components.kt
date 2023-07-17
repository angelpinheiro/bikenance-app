package com.anxops.bkn.data.model

import com.anxops.bkn.util.formatDistanceAsKm
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime


@Serializable
data class ComponentInfo(
    @SerialName("type")
    val type: String,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String = ""
) {
    fun componentType(): ComponentTypes {
        return try {
            ComponentTypes.valueOf(type)
        } catch (e: Exception) {
            throw e
            // TODO: return ComponentTypes.CUSTOM
        }

    }
}

enum class ComponentCategory(val order: Int) {
    TRANSMISSION(1),
    SUSPENSION(2),
    BRAKES(3),
    WHEELS(4),
    MISC(10)
}

// TODO Add: Frame bearings, handlebar tape
enum class ComponentTypes(
    val category: ComponentCategory = ComponentCategory.MISC
) {
    CASSETTE(ComponentCategory.TRANSMISSION),
    CHAIN(ComponentCategory.TRANSMISSION),
    DISC_BRAKE(ComponentCategory.BRAKES),
    DISC_PAD(ComponentCategory.BRAKES),
    DROPER_POST,
    FORK(ComponentCategory.SUSPENSION),
    FRONT_HUB(ComponentCategory.WHEELS),
    PEDAL_CLIPLESS,
    REAR_DERAUILLEURS(ComponentCategory.TRANSMISSION),
    REAR_HUB(ComponentCategory.WHEELS),
    REAR_SUSPENSION(ComponentCategory.SUSPENSION),
    THRU_AXLE,
    TIRE(ComponentCategory.WHEELS),
    WHEELSET(ComponentCategory.WHEELS),
    BRAKE_LEVER(ComponentCategory.BRAKES),
    CABLE_HOUSING(ComponentCategory.TRANSMISSION),
    FRAME_BEARINGS,
    HANDLEBAR_TAPE,
    CUSTOM,
}


fun getDefaultComponents(bikeType: BikeType): Set<ComponentTypes> {
    return maintenanceConfigurations[bikeType] ?: ComponentTypes.values().toSet()
}

val maintenanceConfigurations = mapOf(
    BikeType.MTB to ComponentTypes.values().toSet().minus(
        setOf(
            ComponentTypes.CUSTOM,
            ComponentTypes.DROPER_POST,
            ComponentTypes.REAR_SUSPENSION,
            ComponentTypes.FRAME_BEARINGS,
            ComponentTypes.PEDAL_CLIPLESS,
            ComponentTypes.HANDLEBAR_TAPE,
        )
    ),
    BikeType.FULL_MTB to ComponentTypes.values().toSet().minus(
        setOf(
            ComponentTypes.CUSTOM,
            ComponentTypes.DROPER_POST,
            ComponentTypes.PEDAL_CLIPLESS,
            ComponentTypes.HANDLEBAR_TAPE,
        )
    ),
    BikeType.ROAD to ComponentTypes.values().toSet().minus(
        setOf(
            ComponentTypes.CUSTOM,
            ComponentTypes.DROPER_POST,
            ComponentTypes.REAR_SUSPENSION,
            ComponentTypes.FRAME_BEARINGS,
            ComponentTypes.PEDAL_CLIPLESS,
            ComponentTypes.FORK
        )
    ),
    BikeType.GRAVEL to ComponentTypes.values().toSet().minus(
        setOf(
            ComponentTypes.CUSTOM,
            ComponentTypes.DROPER_POST,
            ComponentTypes.REAR_SUSPENSION,
            ComponentTypes.FRAME_BEARINGS,
            ComponentTypes.PEDAL_CLIPLESS,
            ComponentTypes.FORK
        )
    ),
    BikeType.STATIONARY to setOf()
)


enum class ComponentModifier(val displayName: String) {
    REAR("Rear"),
    FRONT("Front")
}

enum class MaintenanceTypes {
    BRAKE_MAINTENANCE,
    CABLES_AND_HOUSING_MAINTENANCE,
    CASSETTE_MAINTENANCE,
    CHAIN_MAINTENANCE,
    DISC_BRAKE_MAINTENANCE,
    DROPPER_POST_MAINTENANCE,
    FORK_MAINTENANCE,
    FRONT_HUB_MAINTENANCE,
    REAR_SUSPENSION_MAINTENANCE,
    THRU_AXLE_MAINTENANCE,
    TIRE_MAINTENANCE,
    WHEELSET_TUBELESS_MAINTENANCE,
    WHEELSET_WHEELS_AND_SPOKES_MAINTENANCE,
    WHEELSET_TREAD_WEAR_MAINTENANCE
}

enum class RevisionUnit {
    KILOMETERS,
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
data class MaintenanceInfo(
    @SerialName("type")
    val type: String,
    @SerialName("description")
    val description: String,
    @SerialName("longDescription")
    val longDescription: String,
    @SerialName("defaultFrequency")
    val defaultFrequency: RevisionFrequency,
    @SerialName("componentType")
    val componentType: ComponentTypes
) {
    fun maintenanceType(): MaintenanceTypes {
        return try {
            MaintenanceTypes.valueOf(type)
        } catch (e: Exception) {
            throw e
            // TODO: return ComponentTypes.CUSTOM
        }
    }
}

data class Maintenance(
    @SerialName("type")
    val type: MaintenanceInfo,
    @SerialName("usage")
    var usageSinceLastMaintenance: Usage?,
    @SerialName("dueDate")
    var dateTime: String? = null,
)

@Serializable
data class Usage(
    @SerialName("duration")
    val duration: Double,
    @SerialName("distance")
    val distance: Double
)

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