package eu.luminis.workshops.kdd.system.eventstore.keyvalue

interface KeyValueStore<K, V> {
    fun get(key: K): V?
    fun put(key: K, value: V)
}

