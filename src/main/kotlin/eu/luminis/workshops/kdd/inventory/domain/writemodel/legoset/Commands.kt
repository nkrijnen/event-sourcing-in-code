package eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset

import eu.luminis.workshops.kdd.builders.domain.BuilderId
import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber

sealed interface LegoSetCommand {
    val legoSet: LegoSetNumber
}

data class AddLegoSetToCatalogCommand(
    override val legoSet: LegoSetNumber
) : LegoSetCommand

data class BookLegoSetCommand(
    override val legoSet: LegoSetNumber,
    val builder: BuilderId,
) : LegoSetCommand

data class ReturnLegoSetCommand(
    override val legoSet: LegoSetNumber,
    val builder: BuilderId,
) : LegoSetCommand