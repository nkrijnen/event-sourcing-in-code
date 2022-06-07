@file:UseSerializers(InstantAsISO8601::class, LegoSetNumberAsInt::class, BuilderIdAsString::class)

package eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset

import eu.luminis.workshops.kdd.builders.domain.BuilderId
import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber
import eu.luminis.workshops.kdd.system.DomainEvent
import eu.luminis.workshops.kdd.util.json.BuilderIdAsString
import eu.luminis.workshops.kdd.util.json.InstantAsISO8601
import eu.luminis.workshops.kdd.util.json.LegoSetNumberAsInt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

@Serializable
sealed class LegoSetEvent : DomainEvent {
    abstract val legoSet: LegoSetNumber
    abstract val atTime: Instant
}

interface LegoSetInventoryAffectingEvent {
    val legoSet: LegoSetNumber
    val atTime: Instant
    val amountChanged: Int
}

@Serializable
@SerialName("LegoSetAddedToCatalogEvent")
data class LegoSetAddedToCatalogEvent(
    override val legoSet: LegoSetNumber,
    override val atTime: Instant = Instant.now(),
) : LegoSetEvent()

@Serializable
@SerialName("LegoSetBookedEvent")
data class LegoSetBookedEvent(
    override val legoSet: LegoSetNumber,
    val builder: BuilderId,
    override val atTime: Instant = Instant.now(),
) : LegoSetEvent(), LegoSetInventoryAffectingEvent {
    override val amountChanged: Int get() = -1
}

@Serializable
@SerialName("LegoSetReturnedEvent")
data class LegoSetReturnedEvent(
    override val legoSet: LegoSetNumber,
    val builder: BuilderId,
    override val atTime: Instant = Instant.now(),
) : LegoSetEvent(), LegoSetInventoryAffectingEvent {
    override val amountChanged: Int get() = 1
}

@Serializable
@SerialName("LegoSetInventoryDecreasedEvent")
data class LegoSetInventoryDecreasedEvent(
    override val legoSet: LegoSetNumber,
    val amount: Int,
    override val atTime: Instant = Instant.now(),
) : LegoSetEvent(), LegoSetInventoryAffectingEvent {
    override val amountChanged: Int get() = -amount
}

@Serializable
@SerialName("LegoSetInventoryIncreasedEvent")
data class LegoSetInventoryIncreasedEvent(
    override val legoSet: LegoSetNumber,
    val amount: Int,
    override val atTime: Instant = Instant.now(),
) : LegoSetEvent(), LegoSetInventoryAffectingEvent {
    override val amountChanged: Int get() = amount
}
