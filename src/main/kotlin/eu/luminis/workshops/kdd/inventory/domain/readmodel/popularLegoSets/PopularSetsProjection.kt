package eu.luminis.workshops.kdd.inventory.domain.readmodel.popularLegoSets

import eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset.LegoSetBookedEvent
import eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset.LegoSetEvent
import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber
import eu.luminis.workshops.kdd.system.eventbus.EventBus
import java.time.Instant
import java.time.temporal.ChronoUnit.DAYS

class PopularSetsProjection(
    eventBus: EventBus<LegoSetEvent>,
    private val nowProvider: () -> Instant = Instant::now
) {
    private val mostPopularSetsById = mutableMapOf<LegoSetNumber, LegoSetBookingCounts>()

    init {
        eventBus.subscribe(::handleEvent)
    }

    fun mostPopularSets(): List<LegoSetBookingCounts> =
        mostPopularSetsById.values.toList().sortedByDescending { it.sortKey }

    private fun handleEvent(event: LegoSetEvent) {
        when (event) {
            is LegoSetBookedEvent -> updateBookingCounts(event)
            else -> {}
        }
    }

    private fun updateBookingCounts(event: LegoSetBookedEvent) {
        mostPopularSetsById.compute(event.legoSet) { legoSet, accumulatedCounts ->
            val countsForEntry = LegoSetBookingCounts(
                legoSet,
                timesBookedLast2Weeks = last2Weeks().contains(event.atTime).oneIfTrue(),
                timesBooked2WeeksBefore = twoWeeksBefore().contains(event.atTime).oneIfTrue(),
                timesBookedAllTime = 1,
            )
            accumulatedCounts?.merge(countsForEntry) ?: countsForEntry
        }
    }

    private fun Boolean.oneIfTrue() = if (this) 1 else 0

    private fun last2Weeks(): ClosedRange<Instant> {
        return nowProvider().minus(2 * 7, DAYS)..nowProvider()
    }

    private fun twoWeeksBefore(): ClosedRange<Instant> {
        return nowProvider().minus(4 * 7, DAYS)..nowProvider().minus(2 * 7, DAYS)
    }
}

data class LegoSetBookingCounts(
    val legoSet: LegoSetNumber,
    val timesBookedLast2Weeks: Int,
    val timesBooked2WeeksBefore: Int,
    val timesBookedAllTime: Int,
) {
    fun merge(other: LegoSetBookingCounts): LegoSetBookingCounts = copy(
        timesBookedLast2Weeks = this.timesBookedLast2Weeks + other.timesBookedLast2Weeks,
        timesBooked2WeeksBefore = this.timesBooked2WeeksBefore + other.timesBooked2WeeksBefore,
        timesBookedAllTime = this.timesBookedAllTime + other.timesBookedAllTime,
    )

    val sortKey: Int by lazy { timesBookedLast2Weeks * 1000000 + timesBooked2WeeksBefore * 1000 + timesBookedAllTime }
}