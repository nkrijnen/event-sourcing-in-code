package eu.luminis.workshops.kdd.testdata

import eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset.LegoSetEvent
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.io.path.readLines
import kotlin.io.path.writeLines

internal object SampleData {
    val legoSetBookingsAndReturns: Collection<LegoSetEvent> by lazy {
        Path.of("src/test/resources/eu/luminis/workshops/kdd/testdata/sample-bookings.jsonl").readEvents()
    }
}

internal fun Path.writeEvents(events: Collection<LegoSetEvent>) {
    writeLines(events.asSequence().map { Json.encodeToString(it) })
}

internal fun Path.readEvents(): Collection<LegoSetEvent> =
    readLines().map { Json.decodeFromString(it) }