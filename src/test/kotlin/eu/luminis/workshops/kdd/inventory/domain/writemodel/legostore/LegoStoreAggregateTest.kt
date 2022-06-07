package eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore

import eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.commands.StoreJoinCommand
import eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.commands.UpdateCompleteStoreInventoryCommand
import eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.events.CompleteStoreInventoryUpdatedEvent
import eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.events.StoreJoinedEvent
import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber
import eu.luminis.workshops.kdd.testutil.firstAndOnlyItemAs
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.byLessThan
import java.time.Instant.now
import java.time.temporal.ChronoUnit.SECONDS
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class LegoStoreAggregateTest {
    @Test
    fun `should allow new store to join our lego hiring platform`() {
        val store = LegoStoreAggregate(listOf())
        val command = StoreJoinCommand(StoreName("Bricks 4 Everyone"))

        val result = store.handle(command)

        val event = result.firstAndOnlyItemAs<StoreJoinedEvent>()
        assertThat(event.storeName).isEqualTo(StoreName("Bricks 4 Everyone"))
        assertThat(event.joinedOn).isCloseTo(now(), byLessThan(1, SECONDS))
    }

    @Test
    fun `should fail if store already joined`() {
        val store = freshlyJoinedlegoStore()

        assertFailsWith<IllegalStateException> {
            store.handle(StoreJoinCommand(StoreName("Bricks 4 Everyone")))
        }
    }

    @Test
    fun `should allow store to tell us about their current inventory of lego sets`() {
        val store = freshlyJoinedlegoStore()
        val command = UpdateCompleteStoreInventoryCommand(
            store.storeId,
            mapOf(
                LegoSetNumber(10294) to 3,
                LegoSetNumber(10279) to 7,
                LegoSetNumber(42127) to 1,
            )
        )

        val result = store.handle(command)

        val event = result.firstAndOnlyItemAs<CompleteStoreInventoryUpdatedEvent>()
        assertEquals(command.inventoryCountPerSet, event.inventoryCountPerSet)
    }

    @Test
    fun `should allow clearing store inventory`() {
        val store = freshlyJoinedlegoStore()
        val command = UpdateCompleteStoreInventoryCommand(
            store.storeId,
            mapOf()
        )

        val result = store.handle(command)

        val event = result.firstAndOnlyItemAs<CompleteStoreInventoryUpdatedEvent>()
        assertEquals(emptyMap(), event.inventoryCountPerSet)
    }

    @Test
    fun `should fail to update inventory for different store`() {
        val store = freshlyJoinedlegoStore()
        val commandForDifferentStore = UpdateCompleteStoreInventoryCommand(
            StoreId(),
            mapOf(
                LegoSetNumber(10294) to 3,
            )
        )

        assertFailsWith<IllegalArgumentException> { store.handle(commandForDifferentStore) }
    }
}