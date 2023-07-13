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


fun getDefaultComponents(bikeType: BikeType) : Set<ComponentTypes> {
    return maintenanceConfigurations[bikeType] ?: ComponentTypes.values().toSet()
}

val maintenanceConfigurations = mapOf(
    BikeType.MTB to ComponentTypes.values().toSet().minus(
        setOf(
            ComponentTypes.CUSTOM,
            ComponentTypes.DROPER_POST,
            ComponentTypes.REAR_SUSPENSION,
            ComponentTypes.FRAME_BEARINGS,
            ComponentTypes.HANDLEBAR_TAPE,
        )
    ),
    BikeType.FULL_MTB to ComponentTypes.values().toSet().minus(
        setOf(
            ComponentTypes.CUSTOM,
            ComponentTypes.HANDLEBAR_TAPE,
        )
    ),
    BikeType.ROAD to ComponentTypes.values().toSet().minus(
        setOf(
            ComponentTypes.CUSTOM,
            ComponentTypes.DROPER_POST,
            ComponentTypes.REAR_SUSPENSION,
            ComponentTypes.FRAME_BEARINGS,
            ComponentTypes.FORK
        )
    ),
    BikeType.GRAVEL to ComponentTypes.values().toSet().minus(
        setOf(
            ComponentTypes.CUSTOM,
            ComponentTypes.DROPER_POST,
            ComponentTypes.REAR_SUSPENSION,
            ComponentTypes.FRAME_BEARINGS,
            ComponentTypes.FORK
        )
    ),
    BikeType.STATIONARY to setOf()
)

//
//enum class MaintenanceConfigurations(val configName: String, val types: Set<ComponentTypes>) {
//    MTB(
//        "MTB", ComponentTypes.values().toSet().minus(
//            setOf(
//                ComponentTypes.CUSTOM,
//                ComponentTypes.DROPER_POST,
//                ComponentTypes.FRAME_BEARINGS,
//                ComponentTypes.FORK,
//            )
//        )
//    ),
//    FULL_MTB(
//        "Full MTB", ComponentTypes.values().toSet().minus(
//            setOf(
//                ComponentTypes.CUSTOM,
//            )
//        )
//    ),
//    ROAD(
//        "Road", ComponentTypes.values().toSet().minus(
//            setOf(
//                ComponentTypes.CUSTOM,
//                ComponentTypes.REAR_SUSPENSION,
//                ComponentTypes.DROPER_POST,
//                ComponentTypes.FRAME_BEARINGS,
//                ComponentTypes.FORK,
//            )
//        )
//    ),
//    GRAVEL(
//        "Gravel", ComponentTypes.values().toSet().minus(
//            setOf(
//                ComponentTypes.CUSTOM,
//                ComponentTypes.REAR_SUSPENSION,
//                ComponentTypes.DROPER_POST,
//                ComponentTypes.FRAME_BEARINGS,
//                ComponentTypes.FORK,
//            )
//        )
//    )
//}


enum class ComponentModifier {
    REAR,
    FRONT
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
    @SerialName("modifier")
    val modifier: ComponentModifier? = null,
    @SerialName("usage")
    var usage: Usage = Usage(0.0, 0.0)
)