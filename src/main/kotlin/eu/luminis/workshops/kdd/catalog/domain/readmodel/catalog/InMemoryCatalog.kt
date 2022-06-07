package eu.luminis.workshops.kdd.catalog.domain.readmodel.catalog

import eu.luminis.workshops.kdd.inventory.domain.publicevents.InventoryChangedPublicEvent
import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber
import eu.luminis.workshops.kdd.system.IntegrationEvent
import eu.luminis.workshops.kdd.system.eventbus.EventBus

class InMemoryCatalog(initialEntries: List<CatalogEntry>, eventBus: EventBus<IntegrationEvent>) : Catalog {

    private val entries: MutableList<CatalogEntry> = initialEntries.toMutableList()

    init {
        eventBus.subscribe(::trackChangesToAmountInStock)
    }

    override fun search(query: CatalogQuery) = when (query) {
        AllSetsQuery -> entries
        PopularQuery -> entries.filter { it.popular }
    }

    override fun get(legoSet: LegoSetNumber): CatalogEntry? {
        return entries.find { it.id == legoSet }
    }

    private fun trackChangesToAmountInStock(event: IntegrationEvent): Unit = when (event) {
        is InventoryChangedPublicEvent ->
            entries.replaceIf({ it.id == event.legoSet }) { it.adjustAmountInStock(event.amountChanged) }
        else -> {}
    }
}

private fun CatalogEntry.adjustAmountInStock(amountChanged: Int): CatalogEntry =
    this.copy(assumedAmountInStock = this.assumedAmountInStock + amountChanged)

private fun <E> MutableList<E>.replaceIf(predicate: (E) -> Boolean, replaceWith: (E) -> E) {
    this.replaceAll { entry -> if (predicate(entry)) replaceWith(entry) else entry }
}
