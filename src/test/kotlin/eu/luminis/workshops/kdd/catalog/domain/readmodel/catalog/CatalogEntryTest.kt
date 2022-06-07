package eu.luminis.workshops.kdd.catalog.domain.readmodel.catalog

import kotlin.test.Test
import kotlin.test.assertEquals

internal class CatalogEntryTest {

    @Test
    fun `should parse catalog entry from filename`() {
        assertEquals(
            detectiveKantoor(), fromFilename("lego-popular/10246-detectivekantoor.jpg")
        )
        assertEquals(
            kampeerbus(), fromFilename("lego-popular/10279-volkswagen-t2-kampeerbus.jpg")
        )
        assertEquals(
            millenniumFalcon(), fromFilename("lego-starwars/75192-millennium-falcon.jpg")
        )
    }

}