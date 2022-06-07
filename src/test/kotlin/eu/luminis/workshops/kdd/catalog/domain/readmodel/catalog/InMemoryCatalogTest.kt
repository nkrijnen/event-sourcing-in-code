package eu.luminis.workshops.kdd.catalog.domain.readmodel.catalog

import eu.luminis.workshops.kdd.builders.domain.BuilderId
import eu.luminis.workshops.kdd.inventory.domain.publicevents.registerInventoryIntegrationEventPublisher
import eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset.LegoSetBookedEvent
import eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset.LegoSetInventoryIncreasedEvent
import eu.luminis.workshops.kdd.system.DomainEvent
import eu.luminis.workshops.kdd.system.eventbus.EventBus
import eu.luminis.workshops.kdd.system.IntegrationEvent
import org.assertj.core.api.Assertions.assertThat
import java.nio.file.Path
import kotlin.test.Test

internal class InMemoryCatalogTest {

    private val domainEventBus = EventBus<DomainEvent>()
    private val integrationEventBus = EventBus<IntegrationEvent>()
    private val catalog = InMemoryCatalog(popularLegoSets() + starWarsLegoSets(), integrationEventBus)

    init {
        domainEventBus.registerInventoryIntegrationEventPublisher(integrationEventBus)
    }

    @Test
    fun `should find all sets`() {
        val query = AllSetsQuery

        val result = catalog.search(query)

        assertThat(result)
            .hasSize(18)
            .contains(
                detectiveKantoor(),
                kampeerbus(),
                millenniumFalcon(),
            )
    }

    @Test
    fun `should find popular sets`() {
        val query = PopularQuery

        val result = catalog.search(query)

        assertThat(result)
            .hasSize(8)
            .contains(
                detectiveKantoor(),
                kampeerbus(),
            )
            .doesNotContain(
                millenniumFalcon()
            )
    }

    @Test
    fun `should track inventory changes`() {
        val initialStock = catalog.get(millenniumFalcon().id)?.assumedAmountInStock
        assertThat(initialStock).isEqualTo(0)

        domainEventBus.publish(listOf(LegoSetInventoryIncreasedEvent(millenniumFalcon().id, 2)))

        val stockAfterInventoryIncrease = catalog.get(millenniumFalcon().id)?.assumedAmountInStock
        assertThat(stockAfterInventoryIncrease).isEqualTo(2)

        domainEventBus.publish(listOf(LegoSetBookedEvent(millenniumFalcon().id, BuilderId())))

        val stockAfterBooked = catalog.get(millenniumFalcon().id)?.assumedAmountInStock
        assertThat(stockAfterBooked).isEqualTo(1)
    }

    @Test
    fun `should load from disk`() {
        val entries = catalogEntriesFromFileSystem()

        assertThat(entries)
            .hasSize(42)
            .contains(
                detectiveKantoor().withAbsoluteUri(),
                kampeerbus().withAbsoluteUri(),
                millenniumFalcon().withAbsoluteUri(),
            )
    }

}

private fun CatalogEntry.withAbsoluteUri() = this.copy(
    imageUri = Path.of("src/media/catalog").resolve(this.imageUri)
)