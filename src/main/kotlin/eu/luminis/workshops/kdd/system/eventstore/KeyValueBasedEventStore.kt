package eu.luminis.workshops.kdd.system.eventstore

import eu.luminis.workshops.kdd.system.DomainEvent
import eu.luminis.workshops.kdd.system.eventbus.EventBus
import eu.luminis.workshops.kdd.system.eventstore.keyvalue.KeyValueStore

class KeyValueBasedEventStore<ID, E : DomainEvent>(
    private val eventBus: EventBus<DomainEvent>,
    private val keyValueStore: KeyValueStore<ID, List<E>>,
) : EventStore<ID, E> {

    override fun handleMutation(
        aggregateId: ID,
        produceNewEvents: (existingEvents: List<E>) -> /* newEvents */ List<E>
    ) {
        val existingEvents = eventStreamFor(aggregateId)

        val newEvents = produceNewEvents(existingEvents)

        if (newEvents.isNotEmpty()) {
            keyValueStore.put(aggregateId, existingEvents + newEvents)
            eventBus.publish(newEvents)
        }
    }

    private fun eventStreamFor(aggregateId: ID): List<E> = keyValueStore.get(aggregateId) ?: listOf()
}