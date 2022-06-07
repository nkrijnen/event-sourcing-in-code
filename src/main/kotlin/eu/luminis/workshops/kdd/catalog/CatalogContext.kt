package eu.luminis.workshops.kdd.catalog

import eu.luminis.workshops.kdd.catalog.domain.readmodel.catalog.Catalog
import eu.luminis.workshops.kdd.catalog.domain.readmodel.catalog.InMemoryCatalog
import eu.luminis.workshops.kdd.catalog.domain.readmodel.catalog.catalogEntriesFromFileSystem
import eu.luminis.workshops.kdd.catalog.api.defineCatalogApi
import eu.luminis.workshops.kdd.system.IntegrationEvent
import eu.luminis.workshops.kdd.system.eventbus.EventBus
import io.ktor.application.*

class CatalogContext(
    ktorApp: Application,
    integrationEventBus: EventBus<IntegrationEvent>
) {
    private val initialEntries = catalogEntriesFromFileSystem()

    private val catalog: Catalog = InMemoryCatalog(initialEntries, integrationEventBus)

    init {
        ktorApp.defineCatalogApi(catalog)
    }
}