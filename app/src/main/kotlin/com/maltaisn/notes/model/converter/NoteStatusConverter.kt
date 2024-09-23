/*
 * Copyright 2023 Nicolas Maltais
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.errortek.notes.model.converter

import androidx.room.TypeConverter
import com.errortek.notes.model.entity.NoteStatus
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object NoteStatusConverter : KSerializer<NoteStatus> {

    @TypeConverter
    @JvmStatic
    fun toInt(status: NoteStatus) = status.value

    @TypeConverter
    @JvmStatic
    fun toStatus(value: Int) = NoteStatus.fromValue(value)

    override val descriptor = PrimitiveSerialDescriptor("NoteStatus", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: NoteStatus) = encoder.encodeInt(toInt(value))

    override fun deserialize(decoder: Decoder) = toStatus(decoder.decodeInt())
}
