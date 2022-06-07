package eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset

import eu.luminis.workshops.kdd.builders.domain.BuilderId
import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber

// Understands booking of lego sets
class LegoSetAggregateTraditional(private val events: List<LegoSetEvent>) {

    private var amountInStock: Int
    private var available: Boolean

    init {
        require(events.isEmpty() || events.first() is LegoSetAddedToCatalogEvent)

        amountInStock = 0
        for (event in events) {
            if (event is LegoSetInventoryAffectingEvent)
                amountInStock += event.amountChanged
        }

        available = amountInStock > 0
    }

    fun handle(command: LegoSetCommand): List<LegoSetEvent> = command
        .apply(this::requireMatchingLegoSetInCatalog)
        .let(this::handleCommand)

    private fun requireMatchingLegoSetInCatalog(command: LegoSetCommand) {
        if (command !is AddLegoSetToCatalogCommand && legoSet() != command.legoSet) {
            throw DifferentLegoSetException()
        }
    }

    private fun legoSet(): LegoSetNumber {
        if (events.isEmpty()) throw NotInCatalogException()
        return (events.first() as LegoSetAddedToCatalogEvent).legoSet
    }

    private fun handleCommand(command: LegoSetCommand) = when (command) {
        is AddLegoSetToCatalogCommand -> add(command)
        is BookLegoSetCommand -> bookSet(command)
        is ReturnLegoSetCommand -> returnSet(command)
    }

    private fun add(command: AddLegoSetToCatalogCommand) = listOf(LegoSetAddedToCatalogEvent(command.legoSet))

    private fun bookSet(command: BookLegoSetCommand): List<LegoSetEvent> {
        if (!available) throw LegoSetNotAvailableException(command.legoSet)

        return listOf(LegoSetBookedEvent(command.legoSet, command.builder))
    }

    private fun returnSet(command: ReturnLegoSetCommand): List<LegoSetEvent> {
        val indexOfLastBookedByBuilder: Int = events.indexOfLastBookedByBuilderOrNull(command.builder)
            ?: throw NotBookedByBuilderException()
        if (events.eventsAfter(indexOfLastBookedByBuilder).containsReturnedBy(command.builder))
            throw AlreadyReturnedException()

        return listOf(LegoSetReturnedEvent(command.legoSet, command.builder))
    }

}

private fun List<LegoSetEvent>.indexOfLastBookedByBuilderOrNull(builder: BuilderId): Int? =
    this.indexOfLast { it is LegoSetBookedEvent && it.builder == builder }
        .let { if (it == -1) null else it }

private fun List<LegoSetEvent>.eventsAfter(eventIndex: Int): List<LegoSetEvent> =
    this.subList(eventIndex + 1, this.size)

private fun List<LegoSetEvent>.containsReturnedBy(builder: BuilderId): Boolean =
    this.find { it is LegoSetReturnedEvent && it.builder == builder } != null