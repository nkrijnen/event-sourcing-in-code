package eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.events

import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber

data class CompleteStoreInventoryUpdatedEvent(
    val inventoryCountPerSet: Map<LegoSetNumber, Int>
) : StoreEvent