package eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore

import java.util.*

data class StoreId(val id: UUID = UUID.randomUUID()) {
    constructor(storeId: String) : this(UUID.fromString(storeId))
}