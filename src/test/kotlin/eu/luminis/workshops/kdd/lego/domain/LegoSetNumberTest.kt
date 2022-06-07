package eu.luminis.workshops.kdd.lego.domain

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class LegoSetNumberTest {
    @Test
    fun `should accept valid set numbers`() {
        assertDoesNotThrow { LegoSetNumber(100) }
        assertDoesNotThrow { LegoSetNumber(75192) }
        assertDoesNotThrow { LegoSetNumber(9999999) }
    }

    @Test
    fun `should throw for invalid set numbers`() {
        assertFailsWith<IllegalArgumentException> { LegoSetNumber(-1) }
        assertFailsWith<IllegalArgumentException> { LegoSetNumber(0) }
        assertFailsWith<IllegalArgumentException> { LegoSetNumber(1) }
        assertFailsWith<IllegalArgumentException> { LegoSetNumber(99) }
        assertFailsWith<IllegalArgumentException> { LegoSetNumber(10000000) }
    }
}