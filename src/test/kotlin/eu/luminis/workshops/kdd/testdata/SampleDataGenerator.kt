package eu.luminis.workshops.kdd.testdata

import eu.luminis.workshops.kdd.builders.domain.BuilderId
import eu.luminis.workshops.kdd.catalog.domain.readmodel.catalog.catalogEntriesFromFileSystem
import eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset.*
import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber
import java.nio.file.Path
import java.time.Instant
import java.time.Instant.now
import java.time.temporal.ChronoUnit.DAYS

fun main() {
    val generator = LegoSetEventGenerator()
    val events = generator.addedToCatalogEvents() + generator.sampleBookingEvents(27346)
    Path.of("src/test/resources/eu/luminis/workshops/kdd/testdata/sample-bookings.jsonl").writeEvents(events)
}

private class LegoSetEventGenerator {
    private val legoSets: Collection<LegoSetNumber> = catalogEntriesFromFileSystem().map { it.id }
    private val builders: Collection<BuilderId> = (0..50).map { BuilderId() }
    private val now = now()
    private val addedDateRange = now.minusMonths(19) to now.minusMonths(15)
    private val bookingDateRange = now.minusMonths(15) to now

    fun addedToCatalogEvents(): List<LegoSetEvent> = legoSets.flatMap {
        val timeAdded = addedDateRange.random()
        val timeInventoryAvailable = timeAdded.randomTimeAfterDays(7 to 4 * 7)
        val inventoryAdded = (4..9).random()
        listOf(
            LegoSetAddedToCatalogEvent(it, atTime = timeAdded),
            LegoSetInventoryIncreasedEvent(it, amount = inventoryAdded, atTime = timeInventoryAvailable),
        )
    }

    fun sampleBookingEvents(amount: Int): List<LegoSetEvent> {
        return (0..amount).flatMap {
            val timeBooked = bookingDateRange.random()
            val timeReturned = timeBooked.randomTimeAfterDays(1 to 4)
            val randomLegoSet = legoSets.random()
            val randomBuilder = builders.random()
            listOf(
                LegoSetBookedEvent(randomLegoSet, randomBuilder, atTime = timeBooked),
            ).let {
                // skip return if in the future (so we get a few open bookings too)
                if (timeReturned > now) it
                else it + LegoSetReturnedEvent(randomLegoSet, randomBuilder, atTime = timeReturned)
            }
        }
    }
}

private fun Instant.minusMonths(months: Long) = this.minus(months * 30, DAYS)

private fun Instant.randomTimeAfterDays(between: Pair<Number, Number>) =
    (this.plus(between.first.toLong(), DAYS) to this.plus(between.second.toLong(), DAYS)).random()

private fun Pair<Instant, Instant>.random() =
    Instant.ofEpochSecond((first.epochSecond..second.epochSecond).random())