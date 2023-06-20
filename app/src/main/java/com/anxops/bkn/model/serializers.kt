package com.anxops.bkn.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@Serializer(forClass = BikeType::class)
object BikeTypeSerializer : KSerializer<BikeType> {

    override fun serialize(encoder: Encoder, value: BikeType) {
        encoder.encodeString(value.type)
    }

    override fun deserialize(decoder: Decoder): BikeType {
        return when (decoder.decodeString()) {
            BikeType.MTB.type -> BikeType.MTB
            BikeType.ROAD.type -> BikeType.ROAD
            BikeType.E_BIKE.type -> BikeType.E_BIKE
            BikeType.GRAVEL.type -> BikeType.GRAVEL
            else -> BikeType.UNKNOWN
        }
    }
}
