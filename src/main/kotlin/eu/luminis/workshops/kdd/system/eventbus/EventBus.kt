package eu.luminis.workshops.kdd.system.eventbus

class EventBus<E> {
    private val subscribers = mutableSetOf<EventSubscriber<E>>()

    internal fun subscribe(subscriber: EventSubscriber<E>) {
        subscribers.add(subscriber)
    }

    fun publish(event: E) {
        publish(listOf(event))
    }

    fun publish(events: Iterable<E>) {
        events.forEach { event ->
            subscribers.forEach { it(event) }
        }
    }
}

typealias EventSubscriber<E> = (E) -> Unit