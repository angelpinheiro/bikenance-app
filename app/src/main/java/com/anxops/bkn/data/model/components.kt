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


val defaultComponentTypes = mapOf(
    "FRAME" to ComponentType("FRAME", "Frame"),
    "FORK" to ComponentType("FORK", "Front suspension"),
    "HANDLEBAR" to ComponentType("HANDLEBAR", "Handlebar"),
    "BRAKES" to ComponentType("BRAKES", "Brakes"),
    "DERAILLEURS" to ComponentType("DERAILLEURS", "Derailleurs"),
    "CHAIN" to ComponentType("CHAIN", "Chain"),
    "PEDALS" to ComponentType("PEDALS", "Pedals"),
    "RIMS" to ComponentType("RIMS", "Rims"),
    "TIRES" to ComponentType("TIRES", "Tires"),
    "SADDLE" to ComponentType("SADDLE", "Saddle"),
    "CABLES" to ComponentType("CABLES", "Cables and housings"),
    "BOTTOM_BRACKET" to ComponentType("BOTTOM_BRACKET", "Bottom bracket"),
    "HEADSET" to ComponentType("HEADSET", "Headset")
)