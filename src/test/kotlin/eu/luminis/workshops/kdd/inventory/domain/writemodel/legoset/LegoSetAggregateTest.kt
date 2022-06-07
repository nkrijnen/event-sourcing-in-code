package eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset

import eu.luminis.workshops.kdd.builders.domain.BuilderId
import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber
import eu.luminis.workshops.kdd.testutil.firstAndOnlyItemAs
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.byLessThan
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.Instant.now
import java.time.temporal.ChronoUnit.SECONDS
import kotlin.test.assertFailsWith

internal class LegoSetAggregateTest {
    private val millenniumFalcon = LegoSetNumber(75192)
    private val republicGunship = LegoSetNumber(75309)
    private val lars = BuilderId()
    private val anne = BuilderId()

    @Test
    fun `should always start with added-to-catalog event if there are events for this set`() {
        assertDoesNotThrow {
            LegoSetAggregate(listOf())
        }
        assertFailsWith<IllegalArgumentException> {
            listOf(LegoSetInventoryIncreasedEvent(millenniumFalcon, 1)).toLegoSet()
        }
        assertFailsWith<IllegalArgumentException> {
            listOf(LegoSetBookedEvent(millenniumFalcon, BuilderId())).toLegoSet()
        }
    }

    @Test
    fun `should not accept command for different set`() {
        val legoSet = listOf(
            LegoSetAddedToCatalogEvent(millenniumFalcon),
        ).toLegoSet()

        assertFailsWith<DifferentLegoSetException> {
            legoSet.handle(BookLegoSetCommand(republicGunship, BuilderId()))
        }
        assertFailsWith<DifferentLegoSetException> {
            legoSet.handle(ReturnLegoSetCommand(republicGunship, BuilderId()))
        }
    }

    @Nested
    inner class BookingLegoSets {
        @Test
        fun `should book set when available`() {
            val legoSet = listOf(
                LegoSetAddedToCatalogEvent(millenniumFalcon),
                LegoSetInventoryIncreasedEvent(millenniumFalcon, 3),
            ).toLegoSet()
            val command = BookLegoSetCommand(millenniumFalcon, lars)

            val result = legoSet.handle(command)

            val event = result.firstAndOnlyItemAs<LegoSetBookedEvent>()
            Assertions.assertThat(event.legoSet).isEqualTo(millenniumFalcon)
            Assertions.assertThat(event.builder).isEqualTo(lars)
            Assertions.assertThat(event.atTime).isCloseTo(now(), byLessThan(1, SECONDS))
        }

        @Test
        fun `should fail to book set when not in catalog`() {
            val legoSet = LegoSetAggregate(listOf())
            val command = BookLegoSetCommand(millenniumFalcon, BuilderId())

            assertFailsWith<NotInCatalogException> {
                legoSet.handle(command)
            }
        }

        @Test
        fun `should fail to book set when inventory unknown`() {
            val legoSet = listOf(
                LegoSetAddedToCatalogEvent(millenniumFalcon),
            ).toLegoSet()
            val command = BookLegoSetCommand(millenniumFalcon, BuilderId())

            assertFailsWith<LegoSetNotAvailableException> {
                legoSet.handle(command)
            }
        }

        @Test
        fun `should fail to book set when no longer in inventory because it is booked`() {
            val legoSet = listOf(
                LegoSetAddedToCatalogEvent(millenniumFalcon),
                LegoSetInventoryIncreasedEvent(millenniumFalcon, 1),
                LegoSetBookedEvent(millenniumFalcon, BuilderId()),
            ).toLegoSet()
            val command = BookLegoSetCommand(millenniumFalcon, BuilderId())

            assertFailsWith<LegoSetNotAvailableException> {
                legoSet.handle(command)
            }
        }

        @Test
        fun `should fail to book set when no longer in inventory`() {
            val legoSet = listOf(
                LegoSetAddedToCatalogEvent(millenniumFalcon),
                LegoSetInventoryIncreasedEvent(millenniumFalcon, 2),
                LegoSetInventoryDecreasedEvent(millenniumFalcon, 2),
            ).toLegoSet()
            val command = BookLegoSetCommand(millenniumFalcon, BuilderId())

            assertFailsWith<LegoSetNotAvailableException> {
                legoSet.handle(command)
            }
        }

        @Test
        fun `should not be available for booking when all inventory has been booked`() {
            val legoSet = listOf(
                LegoSetAddedToCatalogEvent(millenniumFalcon),
                LegoSetInventoryIncreasedEvent(millenniumFalcon, 2),
                LegoSetBookedEvent(millenniumFalcon, BuilderId()),
                LegoSetBookedEvent(millenniumFalcon, BuilderId()),
            ).toLegoSet()
            val command = BookLegoSetCommand(millenniumFalcon, BuilderId())

            assertFailsWith<LegoSetNotAvailableException> {
                legoSet.handle(command)
            }
        }
    }

    @Nested
    inner class ReturningLegoSets {
        @Test
        fun `should allow returning set after booking it`() {
            val legoSet = listOf(
                LegoSetAddedToCatalogEvent(millenniumFalcon),
                LegoSetInventoryIncreasedEvent(millenniumFalcon, 1),
                LegoSetBookedEvent(millenniumFalcon, lars),
            ).toLegoSet()
            val command = ReturnLegoSetCommand(millenniumFalcon, lars)

            val result = legoSet.handle(command)

            val event = result.firstAndOnlyItemAs<LegoSetReturnedEvent>()
            Assertions.assertThat(event.legoSet).isEqualTo(millenniumFalcon)
            Assertions.assertThat(event.builder).isEqualTo(lars)
            Assertions.assertThat(event.atTime).isCloseTo(now(), byLessThan(1, SECONDS))
        }

        @Test
        fun `should not allow returning set if not booked`() {
            val legoSet = listOf(
                LegoSetAddedToCatalogEvent(millenniumFalcon),
                LegoSetInventoryIncreasedEvent(millenniumFalcon, 1),
            ).toLegoSet()
            val command = ReturnLegoSetCommand(millenniumFalcon, lars)

            assertFailsWith<NotBookedByBuilderException> {
                legoSet.handle(command)
            }
        }

        @Test
        fun `should not allow returning set if not booked by same builder`() {
            val legoSet = listOf(
                LegoSetAddedToCatalogEvent(millenniumFalcon),
                LegoSetInventoryIncreasedEvent(millenniumFalcon, 1),
                LegoSetBookedEvent(millenniumFalcon, anne),
            ).toLegoSet()
            val command = ReturnLegoSetCommand(millenniumFalcon, lars)

            assertFailsWith<NotBookedByBuilderException> {
                legoSet.handle(command)
            }
        }

        @Test
        fun `should not allow returning if already returned after last booking by same builder`() {
            val legoSet = listOf(
                LegoSetAddedToCatalogEvent(millenniumFalcon),
                LegoSetInventoryIncreasedEvent(millenniumFalcon, 1),
                LegoSetBookedEvent(millenniumFalcon, lars),
                LegoSetReturnedEvent(millenniumFalcon, lars),
            ).toLegoSet()
            val command = ReturnLegoSetCommand(millenniumFalcon, lars)

            assertFailsWith<AlreadyReturnedException> {
                legoSet.handle(command)
            }
        }

        @Test
        fun `should be available again for booking after set is returned`() {
            val legoSet = listOf(
                LegoSetAddedToCatalogEvent(millenniumFalcon),
                LegoSetInventoryIncreasedEvent(millenniumFalcon, 1),
                LegoSetBookedEvent(millenniumFalcon, lars),
                LegoSetReturnedEvent(millenniumFalcon, lars),
            ).toLegoSet()
            val command = BookLegoSetCommand(millenniumFalcon, anne)

            assertDoesNotThrow { legoSet.handle(command) }
        }
    }
}

private fun List<LegoSetEvent>.toLegoSet() = LegoSetAggregate(this)