package com.anxops.bkn.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ComponentType(
    @SerialName("type")
    val type: String,
    @SerialName("name")
    val name: String
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
    FRAME,
    FORK,
    HANDLEBAR,
    BRAKES,
    DERAILLEURS,
    CHAIN,
    PEDALS,
    RIMS,
    TIRES,
    SADDLE,
    CABLES,
    BOTTOM_BRACKET,
    HEADSET
}


val defaultComponentTypes = mapOf(
    BikeComponentType.FRAME to ComponentType(BikeComponentType.FRAME.name, "Frame"),
    BikeComponentType.FORK to ComponentType(BikeComponentType.FORK.name, "Front suspension"),
    BikeComponentType.HANDLEBAR to ComponentType(BikeComponentType.HANDLEBAR.name, "Handlebar"),
    BikeComponentType.BRAKES to ComponentType(BikeComponentType.BRAKES.name, "Brakes"),
    BikeComponentType.DERAILLEURS to ComponentType(BikeComponentType.DERAILLEURS.name, "Derailleurs"),
    BikeComponentType.CHAIN to ComponentType(BikeComponentType.CHAIN.name, "Chain"),
    BikeComponentType.PEDALS to ComponentType(BikeComponentType.PEDALS.name, "Pedals"),
    BikeComponentType.RIMS to ComponentType(BikeComponentType.RIMS.name, "Rims"),
    BikeComponentType.TIRES to ComponentType(BikeComponentType.TIRES.name, "Tires"),
    BikeComponentType.SADDLE to ComponentType(BikeComponentType.SADDLE.name, "Saddle"),
    BikeComponentType.CABLES to ComponentType(BikeComponentType.CABLES.name, "Cables and housings"),
    BikeComponentType.BOTTOM_BRACKET to ComponentType(BikeComponentType.BOTTOM_BRACKET.name, "Bottom bracket"),
    BikeComponentType.HEADSET to ComponentType(BikeComponentType.HEADSET.name, "Headset")
)