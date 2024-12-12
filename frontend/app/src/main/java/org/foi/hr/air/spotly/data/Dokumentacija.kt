package org.foi.hr.air.spotly.data

import android.util.Base64
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ByteArrayAsBase64Serializer : KSerializer<ByteArray> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Base64ByteArray", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ByteArray) {
        encoder.encodeString(Base64.encodeToString(value, Base64.NO_WRAP))
    }

    override fun deserialize(decoder: Decoder): ByteArray {
        return Base64.decode(decoder.decodeString(), Base64.DEFAULT)
    }
}

@Serializable
data class Dokumentacija(
    val id: Int,
    @Serializable(with = ByteArrayAsBase64Serializer::class) val slika: ByteArray?,
    val kaznaId: Int?,
    val zahtjevId: Int?
)