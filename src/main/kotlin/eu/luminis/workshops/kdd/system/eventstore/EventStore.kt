package eu.luminis.workshops.kdd.system.eventstore

interface EventStore<ID, E> {
    /**
     * Transparently handles everything needed when a mutation comes in.
     *
     * Fetches existing events for the aggregate, passes them to the mutation handler that will produce new events.
     * When done, the new events are appended to the event stream.
     */
    fun handleMutation(aggregateId: ID, produceNewEvents: (List<E>) -> List<E>)
}