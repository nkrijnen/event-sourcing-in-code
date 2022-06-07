package eu.luminis.workshops.kdd.inventory.domain

import eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.StoreId
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

internal class StoreIdTest {
    @Test
    fun `should generate unique id when creating new store id`() {
        val firstNewStoreId = StoreId()
        val secondNewStoreId = StoreId()
        assertNotEquals(firstNewStoreId, secondNewStoreId)
    }

    @Test
    fun `should accept existing store id`() {
        assertDoesNotThrow {
            StoreId("123e4567-e89b-12d3-a456-426614174000")
        }
    }

    @Test
    fun `should fail to accept existing store id with invalid format`() {
        assertFailsWith<IllegalArgumentException> {
            StoreId("wrong")
        }
    }
}