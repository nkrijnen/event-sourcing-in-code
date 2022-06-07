package eu.luminis.workshops.kdd.inventory.domain.readmodel.popularLegoSets

import eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset.LegoSetEvent
import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber
import eu.luminis.workshops.kdd.system.eventbus.EventBus
import eu.luminis.workshops.kdd.testdata.SampleData
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class PopularSetsProjectionTest {
    @Test
    fun `should produce lego set popularity report for stores`() {
        val eventBus = EventBus<LegoSetEvent>()
        val projection = PopularSetsProjection(eventBus) { Instant.parse("2022-04-13T16:00:00Z") }

        eventBus.publish(SampleData.legoSetBookingsAndReturns)
        val result = projection.mostPopularSets()
//        printReadableResults(result)

        val expected = listOf(
            LegoSetBookingCounts(LegoSetNumber(75308), 29, 25, 651),
            LegoSetBookingCounts(LegoSetNumber(42141), 28, 19, 677),
            LegoSetBookingCounts(LegoSetNumber(42114), 27, 24, 645),
            LegoSetBookingCounts(LegoSetNumber(21330), 26, 24, 653),
            LegoSetBookingCounts(LegoSetNumber(75309), 25, 22, 663),
            LegoSetBookingCounts(LegoSetNumber(42098), 24, 29, 648),
            LegoSetBookingCounts(LegoSetNumber(75978), 24, 16, 634),
            LegoSetBookingCounts(LegoSetNumber(10246), 23, 32, 665),
            LegoSetBookingCounts(LegoSetNumber(75290), 23, 27, 673),
            LegoSetBookingCounts(LegoSetNumber(75288), 23, 20, 664),
            LegoSetBookingCounts(LegoSetNumber(75192), 23, 18, 667),
            LegoSetBookingCounts(LegoSetNumber(42111), 23, 17, 622),
            LegoSetBookingCounts(LegoSetNumber(42128), 23, 17, 619),
            LegoSetBookingCounts(LegoSetNumber(10279), 22, 23, 633),
            LegoSetBookingCounts(LegoSetNumber(42056), 22, 22, 712),
            LegoSetBookingCounts(LegoSetNumber(75313), 22, 22, 661),
            LegoSetBookingCounts(LegoSetNumber(10284), 22, 20, 650),
            LegoSetBookingCounts(LegoSetNumber(42129), 22, 19, 662),
            LegoSetBookingCounts(LegoSetNumber(76178), 22, 18, 661),
            LegoSetBookingCounts(LegoSetNumber(42110), 22, 17, 682),
            LegoSetBookingCounts(LegoSetNumber(75159), 21, 28, 656),
            LegoSetBookingCounts(LegoSetNumber(75244), 21, 17, 645),
            LegoSetBookingCounts(LegoSetNumber(42055), 21, 17, 594),
            LegoSetBookingCounts(LegoSetNumber(42078), 21, 16, 632),
            LegoSetBookingCounts(LegoSetNumber(42070), 20, 27, 657),
            LegoSetBookingCounts(LegoSetNumber(42115), 20, 23, 634),
            LegoSetBookingCounts(LegoSetNumber(42108), 20, 17, 643),
            LegoSetBookingCounts(LegoSetNumber(75252), 20, 15, 623),
            LegoSetBookingCounts(LegoSetNumber(42043), 19, 24, 653),
            LegoSetBookingCounts(LegoSetNumber(42125), 19, 17, 621),
            LegoSetBookingCounts(LegoSetNumber(42096), 18, 16, 735),
            LegoSetBookingCounts(LegoSetNumber(42082), 17, 21, 677),
            LegoSetBookingCounts(LegoSetNumber(42083), 17, 20, 621),
            LegoSetBookingCounts(LegoSetNumber(42131), 17, 19, 615),
            LegoSetBookingCounts(LegoSetNumber(42130), 17, 18, 666),
            LegoSetBookingCounts(LegoSetNumber(10294), 17, 15, 639),
            LegoSetBookingCounts(LegoSetNumber(42127), 16, 17, 600),
            LegoSetBookingCounts(LegoSetNumber(75275), 14, 21, 675),
            LegoSetBookingCounts(LegoSetNumber(42099), 14, 21, 653),
            LegoSetBookingCounts(LegoSetNumber(42100), 14, 18, 667),
            LegoSetBookingCounts(LegoSetNumber(31120), 12, 19, 654),
            LegoSetBookingCounts(LegoSetNumber(42126), 12, 17, 645),
        )
        assertEquals(expected, result)
    }

    private fun printReadableResults(result: List<LegoSetBookingCounts>) {
        result.forEach { println("LegoSetBookingCounts(LegoSetNumber(${it.legoSet}), ${it.timesBookedLast2Weeks}, ${it.timesBooked2WeeksBefore}, ${it.timesBookedAllTime}),") }
    }
}