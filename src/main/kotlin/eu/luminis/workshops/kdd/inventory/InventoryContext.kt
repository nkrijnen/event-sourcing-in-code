package eu.luminis.workshops.kdd.inventory

import eu.luminis.workshops.kdd.catalog.domain.readmodel.catalog.catalogEntriesFromFileSystem
import eu.luminis.workshops.kdd.catalog.domain.readmodel.catalog.initialEventsForEntry
import eu.luminis.workshops.kdd.inventory.domain.LegoSetService
import eu.luminis.workshops.kdd.inventory.domain.publicevents.registerInventoryIntegrationEventPublisher
import eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset.LegoSetEvent
import eu.luminis.workshops.kdd.inventory.api.defineLegoSetApi
import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber
import eu.luminis.workshops.kdd.system.DomainEvent
import eu.luminis.workshops.kdd.system.IntegrationEvent
import eu.luminis.workshops.kdd.system.eventbus.EventBus
import eu.luminis.workshops.kdd.system.eventstore.EventStore
import eu.luminis.workshops.kdd.system.eventstore.KeyValueBasedEventStore
import eu.luminis.workshops.kdd.system.eventstore.keyvalue.InMemoryKeyValueStore
import eu.luminis.workshops.kdd.system.eventstore.keyvalue.KeyValueStore
import io.ktor.application.*

class InventoryContext(
    ktorApp: Application,
    integrationEventBus: EventBus<IntegrationEvent>
) {
    private val domainEventBus = EventBus<DomainEvent>()

    private val keyValueStore: KeyValueStore<LegoSetNumber, List<LegoSetEvent>> = InMemoryKeyValueStore(
        catalogEntriesFromFileSystem().associate { it.id to initialEventsForEntry(it) }
    )

    private val eventStore: EventStore<LegoSetNumber, LegoSetEvent> = KeyValueBasedEventStore(
        domainEventBus,
        keyValueStore,
    )

    private val legoSetService = LegoSetService(eventStore)

    init {
        domainEventBus.registerInventoryIntegrationEventPublisher(integrationEventBus)

        ktorApp.defineLegoSetApi(legoSetService)
    }
}