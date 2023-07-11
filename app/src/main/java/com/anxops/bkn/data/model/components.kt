package com.anxops.bkn.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


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

enum class ComponentGroup(val order: Int) {
    TRANSMISSION(1),
    SUSPENSION(2),
    BRAKES(3),
    TIRES(4),
    OTHER(10)
}

// TODO Add: Frame bearings, handlebar tape
enum class ComponentTypes(val group: ComponentGroup = ComponentGroup.OTHER) {
    BRAKE_LEVER(ComponentGroup.BRAKES),
    CABLE_HOUSING,
    CASSETTE(ComponentGroup.TRANSMISSION),
    CHAIN(ComponentGroup.TRANSMISSION),
    DISC_BRAKE(ComponentGroup.BRAKES),
    DISC_PAD(ComponentGroup.BRAKES),
    DROPER_POST,
    FORK(ComponentGroup.SUSPENSION),
    FRONT_HUB(ComponentGroup.TIRES),
    PEDAL_CLIPLESS,
    REAR_DERAUILLEURS(ComponentGroup.TRANSMISSION),
    REAR_HUB(ComponentGroup.TIRES),
    REAR_SUSPENSION(ComponentGroup.SUSPENSION),
    THRU_AXLE(ComponentGroup.TIRES),
    TIRE(ComponentGroup.TIRES),
    WHEELSET(ComponentGroup.TIRES),
    CUSTOM,
    UNKNOWN
}

enum class MaintenanceConfigurations(val configName: String, val types: Set<ComponentTypes>) {
    MTB(
        "MTB", ComponentTypes.values().toSet().minus(
            setOf(
                ComponentTypes.CUSTOM,
                ComponentTypes.UNKNOWN,
                ComponentTypes.DROPER_POST,
                ComponentTypes.FORK,
            )
        )
    ),
    FULL_MTB(
        "Full MTB", ComponentTypes.values().toSet().minus(
            setOf(
                ComponentTypes.CUSTOM,
                ComponentTypes.UNKNOWN,
            )
        )
    ),
    ROAD(
        "Road", ComponentTypes.values().toSet().minus(
            setOf(
                ComponentTypes.CUSTOM,
                ComponentTypes.UNKNOWN,
                ComponentTypes.REAR_SUSPENSION,
                ComponentTypes.DROPER_POST,
                ComponentTypes.FORK,
            )
        )
    ),
    GRAVEL(
        "Gravel", ComponentTypes.values().toSet().minus(
            setOf(
                ComponentTypes.CUSTOM,
                ComponentTypes.UNKNOWN,
                ComponentTypes.REAR_SUSPENSION,
                ComponentTypes.DROPER_POST,
                ComponentTypes.FORK,
            )
        )
    )
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
    var usageSinceLastMaintenance: Usage = Usage(0.0, 0.0),
    @SerialName("dueDate")
    var dateTime: String? = null,
)

@Serializable
data class Usage(
    @SerialName("hours")
    val hours: Double,
    @SerialName("km")
    val km: Double
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
    @SerialName("usage")
    var usage: Usage = Usage(0.0, 0.0)
)