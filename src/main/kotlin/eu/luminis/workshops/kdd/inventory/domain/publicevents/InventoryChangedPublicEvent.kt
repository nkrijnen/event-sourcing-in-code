package eu.luminis.workshops.kdd.inventory.domain.publicevents

import eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset.LegoSetInventoryAffectingEvent
import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber
import eu.luminis.workshops.kdd.system.IntegrationEvent
import java.time.Instant

data class InventoryChangedPublicEvent(
    val legoSet: LegoSetNumber,
    val amountChanged: Int,
    val atTime: Instant
) : IntegrationEvent {
    internal constructor(domainEvent: LegoSetInventoryAffectingEvent) : this(
        domainEvent.legoSet,
        domainEvent.amountChanged,
        domainEvent.atTime
    )
}