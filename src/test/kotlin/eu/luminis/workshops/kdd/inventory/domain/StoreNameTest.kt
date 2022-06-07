package eu.luminis.workshops.kdd.inventory.domain

import eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore.StoreName
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class StoreNameTest {
    @Test
    fun `should accept valid store names`() {
        assertDoesNotThrow { StoreName("Lego 4 Hire") }
        assertDoesNotThrow { StoreName("Brick Land") }
    }

    @Test
    fun `should fail when store name is not valid`() {
        assertFailsWith<IllegalArgumentException> { StoreName("") }
        assertFailsWith<IllegalArgumentException> { StoreName("    ") }
        assertFailsWith<IllegalArgumentException> { StoreName("  \n  \t  \r\n  ") }
    }

    @Test
    fun `should fail when store name starts or ends with whitespace`() {
        assertFailsWith<IllegalArgumentException> { StoreName("  Brick Land") }
        assertFailsWith<IllegalArgumentException> { StoreName("Brick Land  \n ") }
    }
}