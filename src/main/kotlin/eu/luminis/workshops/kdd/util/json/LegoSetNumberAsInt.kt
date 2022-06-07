package eu.luminis.workshops.kdd.util.json

import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object LegoSetNumberAsInt : KSerializer<LegoSetNumber> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LegoSetNumber", PrimitiveKind.INT)
    override fun serialize(encoder: Encoder, value: LegoSetNumber) = encoder.encodeInt(value.number)
    override fun deserialize(decoder: Decoder): LegoSetNumber = LegoSetNumber(decoder.decodeInt())
}