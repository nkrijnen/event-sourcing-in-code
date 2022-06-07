package eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.events

import eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.StoreId
import eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.StoreName
import java.time.Instant

class StoreJoinedEvent(
    val storeId: StoreId,
    val storeName: StoreName,
    val joinedOn: Instant = Instant.now(),
) : StoreEvent