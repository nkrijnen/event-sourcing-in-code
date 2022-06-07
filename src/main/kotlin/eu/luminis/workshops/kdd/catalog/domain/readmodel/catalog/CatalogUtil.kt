package eu.luminis.workshops.kdd.catalog.domain.readmodel.catalog

import eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset.LegoSetAddedToCatalogEvent
import eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset.LegoSetInventoryIncreasedEvent
import java.nio.file.Files
import java.nio.file.Path

fun catalogEntriesFromFileSystem(root: Path = Path.of("src/media/catalog")): List<CatalogEntry> = Files.walk(root)
    .filter { it.toString().endsWith("jpg") }
    .map { fromPath(it) }
    .toList()

fun initialEventsForEntry(entry: CatalogEntry) = listOf(
    LegoSetAddedToCatalogEvent(entry.id)
).let { events ->
    if (entry.popular)
        events + LegoSetInventoryIncreasedEvent(entry.id, 42)
    else events
}