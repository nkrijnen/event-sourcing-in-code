package eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.commands

import eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.StoreId
import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber

data class UpdateCompleteStoreInventoryCommand(
    val store: StoreId,
    val inventoryCountPerSet: Map<LegoSetNumber, Int>
) : StoreCommand