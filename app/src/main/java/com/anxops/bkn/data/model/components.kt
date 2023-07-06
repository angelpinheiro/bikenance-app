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
        }catch (e: Exception) {
            throw e
            // TODO: return ComponentTypes.CUSTOM
        }

    }
}

enum class ComponentTypes {
    BRAKE_LEVER,
    CABLE_HOUSING,
    CASSETTE,
    CHAIN,
    DISC_BRAKE,
    DISC_PAD,
    DROPER_POST,
    FORK,
    FRONT_HUB,
    PEDAL_CLIPLESS,
    REAR_DERAUILLEURS,
    REAR_HUB,
    REAR_SUSPENSION,
    THRU_AXLE,
    TIRE,
    WHEELSET,
    CUSTOM,
    UNKNOWN
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
){
    fun maintenanceType(): MaintenanceTypes {
        return try {
            MaintenanceTypes.valueOf(type)
        }catch (e: Exception) {
            throw e
            // TODO: return ComponentTypes.CUSTOM
        }
    }
}

data class Maintenance(
    @SerialName("type")
    val type: MaintenanceInfo,
    @SerialName("usage")
    var usageSinceLastMaintenance: Usage = Usage(0.0,0.0),
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
    val _id: String,
    @SerialName("bikeId")
    val bikeId: String?,
    @SerialName("alias")
    val alias: String? = null,
    @SerialName("info")
    val info: ComponentInfo,
    @SerialName("usage")
    var usage: Usage = Usage(0.0, 0.0)
)
//
//val defaultComponentTypes = mapOf(
//
//    ComponentTypes.BRAKE_LEVER to ComponentInfo(
//        ComponentTypes.BRAKE_LEVER.name,
//        "Brake Lever",
//        "Controls the bike's braking system."
//    ),
//    ComponentTypes.CASSETTE to ComponentInfo(
//        ComponentTypes.CASSETTE.name,
//        "Cassette",
//        "Gears on the rear wheel hub."
//    ),
//    ComponentTypes.DISC_BRAKE to ComponentInfo(
//        ComponentTypes.DISC_BRAKE.name,
//        "Brake Disc",
//        "Braking system using disc rotor and caliper."
//    ),
//    ComponentTypes.FORK to ComponentInfo(
//        ComponentTypes.FORK.name,
//        "Fork",
//        "Front wheel holder with suspension and steering."
//    ),
//    ComponentTypes.PEDAL_CLIPLESS to ComponentInfo(
//        ComponentTypes.PEDAL_CLIPLESS.name,
//        "Clipless Pedal",
//        "Securely attaches shoes to pedals."
//    ),
//    ComponentTypes.REAR_HUB to ComponentInfo(
//        ComponentTypes.REAR_HUB.name,
//        "Rear Hub",
//        "Center of rear wheel with bearings."
//    ),
//    ComponentTypes.THRU_AXLE to ComponentInfo(
//        ComponentTypes.THRU_AXLE.name,
//        "Thru Axle",
//        "Stiff and secure axle for wheels."
//    ),
//    ComponentTypes.WHEELSET to ComponentInfo(
//        ComponentTypes.WHEELSET.name,
//        "Wheelset",
//        "Pair of wheels including rims, spokes, and hubs."
//    ),
//    ComponentTypes.CABLE_HOUSING to ComponentInfo(
//        ComponentTypes.CABLE_HOUSING.name,
//        "Cable Housing & Cables",
//        "Shifting and braking cables and their protective covering."
//    ),
//    ComponentTypes.CHAIN to ComponentInfo(
//        ComponentTypes.CHAIN.name,
//        "Chain",
//        "Transfers power from pedals to rear wheel."
//    ),
//    ComponentTypes.DISC_PAD to ComponentInfo(
//        ComponentTypes.DISC_PAD.name,
//        "Disc Pad",
//        "Brake pads for disc brakes."
//    ),
//    ComponentTypes.DROPER_POST to ComponentInfo(
//        ComponentTypes.DROPER_POST.name,
//        "Dropper Post",
//        "Adjustable seatpost for on-the-fly height changes."
//    ),
//    ComponentTypes.FRONT_HUB to ComponentInfo(
//        ComponentTypes.FRONT_HUB.name,
//        "Front Hub",
//        "Center of front wheel with bearings."
//    ),
//    ComponentTypes.REAR_DERAUILLEURS to ComponentInfo(
//        ComponentTypes.REAR_DERAUILLEURS.name,
//        "Rear Derailleurs",
//        "Shifts the chain between gears at the rear wheel."
//    ),
//    ComponentTypes.REAR_SUSPENSION to ComponentInfo(
//        ComponentTypes.REAR_SUSPENSION.name,
//        "Rear Suspension",
//        "Provides suspension at the rear wheel."
//    ),
//    ComponentTypes.TIRE to ComponentInfo(
//        ComponentTypes.TIRE.name,
//        "Tire",
//        "Rubber outer layer for the wheels."
//    )
//)