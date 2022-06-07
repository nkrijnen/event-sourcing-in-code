package eu.luminis.workshops.kdd.util.json

import eu.luminis.workshops.kdd.builders.domain.BuilderId
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

object BuilderIdAsString : KSerializer<BuilderId> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("BuilderId", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: BuilderId) = encoder.encodeString(value.id.toString())
    override fun deserialize(decoder: Decoder): BuilderId = BuilderId(UUID.fromString(decoder.decodeString()))
}