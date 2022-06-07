package eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore

import eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.events.StoreJoinedEvent

internal fun freshlyJoinedlegoStore() = LegoStoreAggregate(
    listOf(
        StoreJoinedEvent(StoreId(), StoreName("First!"))
    )
)