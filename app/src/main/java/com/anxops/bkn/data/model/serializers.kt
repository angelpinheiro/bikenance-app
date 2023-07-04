/*
 * Copyright 2023 Angel Piñeiro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anxops.bkn.data.model

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
