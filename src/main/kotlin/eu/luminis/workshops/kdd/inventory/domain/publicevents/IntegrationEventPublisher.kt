package eu.luminis.workshops.kdd.inventory.domain.publicevents

import eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset.LegoSetInventoryAffectingEvent
import eu.luminis.workshops.kdd.system.DomainEvent
import eu.luminis.workshops.kdd.system.IntegrationEvent
import eu.luminis.workshops.kdd.system.eventbus.EventBus

fun EventBus<DomainEvent>.registerInventoryIntegrationEventPublisher(
    integrationEventBus: EventBus<IntegrationEvent>
) {
    this.subscribe {
        when (it) {
            is LegoSetInventoryAffectingEvent -> integrationEventBus.publish(InventoryChangedPublicEvent(it))
        }
    }
}