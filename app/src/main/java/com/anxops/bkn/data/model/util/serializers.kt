package com.anxops.bkn.data.model.util

import android.util.Log
import com.anxops.bkn.data.model.BikeType
import com.anxops.bkn.data.model.ComponentType
import com.anxops.bkn.data.model.MaintenanceType
import com.anxops.bkn.util.formatAsIso8061
import com.anxops.bkn.util.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime


@Serializer(forClass = LocalDateTime::class)
object LocalDateSerializer : KSerializer<LocalDateTime> {
    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.formatAsIso8061())
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return decoder.decodeString().toLocalDateTime()
    }
}

@Serializer(forClass = LocalDateTime::class)
object ComponentTypeSerializer : KSerializer<ComponentType> {
    override fun serialize(encoder: Encoder, value: ComponentType) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): ComponentType {
        return ComponentType.getByName(decoder.decodeString())
    }
}

@Serializer(forClass = LocalDateTime::class)
object MaintenanceTypeSerializer : KSerializer<MaintenanceType> {
    override fun serialize(encoder: Encoder, value: MaintenanceType) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): MaintenanceType {
        return MaintenanceType.getByName(decoder.decodeString())
    }
}

@Serializer(forClass = LocalDateTime::class)
object BikeTypeSerializer : KSerializer<BikeType> {
    override fun serialize(encoder: Encoder, value: BikeType) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): BikeType {
        return BikeType.getByName(decoder.decodeString())
    }
}
