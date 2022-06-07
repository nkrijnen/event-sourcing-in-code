package eu.luminis.workshops.kdd.inventory.domain

import eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset.LegoSetAggregate
import eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset.LegoSetCommand
import eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset.LegoSetEvent
import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber
import eu.luminis.workshops.kdd.system.eventstore.EventStore

class LegoSetService(
    private val eventStore: EventStore<LegoSetNumber, LegoSetEvent>
) {
    fun handleCommand(command: LegoSetCommand) {
        eventStore.handleMutation(command.legoSet) { existingEvents ->
            LegoSetAggregate(existingEvents).handle(command)
        }
    }
}