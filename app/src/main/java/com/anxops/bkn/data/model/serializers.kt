package com.anxops.bkn.data.model

import android.util.Log
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
        Log.d("LocalDateSerializer", "$value")
        encoder.encodeString(value.formatAsIso8061())
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return decoder.decodeString().toLocalDateTime()
    }
}
