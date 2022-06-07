package eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore

import eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.commands.StoreCommand
import eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.commands.StoreJoinCommand
import eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.commands.UpdateCompleteStoreInventoryCommand
import eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.events.CompleteStoreInventoryUpdatedEvent
import eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.events.StoreEvent
import eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.events.StoreJoinedEvent

// Aggregate root for lego store
class LegoStoreAggregate(private val events: List<StoreEvent>) {

    val storeId: StoreId
        get() = (events[0] as StoreJoinedEvent).storeId

    fun handle(command: StoreCommand): List<StoreEvent> = when (command) {
        is StoreJoinCommand -> join(command)
        is UpdateCompleteStoreInventoryCommand -> updateCompleteStoreInventory(command)
    }

    private fun join(command: StoreJoinCommand): List<StoreEvent> {
        check(events.isEmpty()) { "Store has already joined" }

        return listOf(StoreJoinedEvent(StoreId(), command.storeName))
    }

    private fun updateCompleteStoreInventory(command: UpdateCompleteStoreInventoryCommand): List<StoreEvent> {
        require(command.store == storeId) { "Can not update inventory of different store" }

        return listOf(CompleteStoreInventoryUpdatedEvent(command.inventoryCountPerSet))
    }

}