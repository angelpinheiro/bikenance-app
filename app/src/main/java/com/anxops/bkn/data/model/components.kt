package com.anxops.bkn.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ComponentType(
    @SerialName("type")
    val type: String,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String = ""
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
    @SerialName("description")
    val description: String? = null,
    @SerialName("type")
    val type: ComponentType,
    @SerialName("usage")
    var usage: Usage = Usage(0.0, 0.0)
)


enum class BikeComponentType {
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
    WHEELSET
}

val defaultComponentTypes = mapOf(

    BikeComponentType.BRAKE_LEVER to ComponentType(
        BikeComponentType.BRAKE_LEVER.name,
        "Brake Lever",
        "Controls the bike's braking system."
    ),
    BikeComponentType.CASSETTE to ComponentType(
        BikeComponentType.CASSETTE.name,
        "Cassette",
        "Gears on the rear wheel hub."
    ),
    BikeComponentType.DISC_BRAKE to ComponentType(
        BikeComponentType.DISC_BRAKE.name,
        "Brake Disc",
        "Braking system using disc rotor and caliper."
    ),
    BikeComponentType.FORK to ComponentType(
        BikeComponentType.FORK.name,
        "Fork",
        "Front wheel holder with suspension and steering."
    ),
    BikeComponentType.PEDAL_CLIPLESS to ComponentType(
        BikeComponentType.PEDAL_CLIPLESS.name,
        "Clipless Pedal",
        "Securely attaches shoes to pedals."
    ),
    BikeComponentType.REAR_HUB to ComponentType(
        BikeComponentType.REAR_HUB.name,
        "Rear Hub",
        "Center of rear wheel with bearings."
    ),
    BikeComponentType.THRU_AXLE to ComponentType(
        BikeComponentType.THRU_AXLE.name,
        "Thru Axle",
        "Stiff and secure axle for wheels."
    ),
    BikeComponentType.WHEELSET to ComponentType(
        BikeComponentType.WHEELSET.name,
        "Wheelset",
        "Pair of wheels including rims, spokes, and hubs."
    ),
    BikeComponentType.CABLE_HOUSING to ComponentType(
        BikeComponentType.CABLE_HOUSING.name,
        "Cable Housing & Cables",
        "Shifting and braking cables and their protective covering."
    ),
    BikeComponentType.CHAIN to ComponentType(
        BikeComponentType.CHAIN.name,
        "Chain",
        "Transfers power from pedals to rear wheel."
    ),
    BikeComponentType.DISC_PAD to ComponentType(
        BikeComponentType.DISC_PAD.name,
        "Disc Pad",
        "Brake pads for disc brakes."
    ),
    BikeComponentType.DROPER_POST to ComponentType(
        BikeComponentType.DROPER_POST.name,
        "Dropper Post",
        "Adjustable seatpost for on-the-fly height changes."
    ),
    BikeComponentType.FRONT_HUB to ComponentType(
        BikeComponentType.FRONT_HUB.name,
        "Front Hub",
        "Center of front wheel with bearings."
    ),
    BikeComponentType.REAR_DERAUILLEURS to ComponentType(
        BikeComponentType.REAR_DERAUILLEURS.name,
        "Rear Derailleurs",
        "Shifts the chain between gears at the rear wheel."
    ),
    BikeComponentType.REAR_SUSPENSION to ComponentType(
        BikeComponentType.REAR_SUSPENSION.name,
        "Rear Suspension",
        "Provides suspension at the rear wheel."
    ),
    BikeComponentType.TIRE to ComponentType(
        BikeComponentType.TIRE.name,
        "Tire",
        "Rubber outer layer for the wheels."
    )
)