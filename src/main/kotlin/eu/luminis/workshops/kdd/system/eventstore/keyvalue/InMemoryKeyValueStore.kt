package eu.luminis.workshops.kdd.system.eventstore.keyvalue

import java.util.concurrent.ConcurrentHashMap

class InMemoryKeyValueStore<K, V>(initialEntries: Map<K, V> = mapOf()) : KeyValueStore<K, V> {
    private val store: MutableMap<K, V> = ConcurrentHashMap(initialEntries)

    override fun get(key: K): V? = store[key]

    override fun put(key: K, value: V) {
        store[key] = value
    }
}