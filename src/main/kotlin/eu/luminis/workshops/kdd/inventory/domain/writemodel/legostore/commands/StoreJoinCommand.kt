package eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.commands

import eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.StoreName

data class StoreJoinCommand(
    val storeName: StoreName
) : StoreCommand

