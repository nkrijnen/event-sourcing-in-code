package eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset

import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber

class AlreadyReturnedException() :
    IllegalStateException("Cannot return this Lego set, it has already been returned by this builder")

class DifferentLegoSetException() :
    IllegalArgumentException("Command cannot be for a different lego set")

class LegoSetNotAvailableException(legoSet: LegoSetNumber) :
    IllegalStateException("Lego set $legoSet is not available at this moment")

class NotBookedByBuilderException() :
    IllegalStateException("Cannot return this Lego set, it was not booked by this builder")

class NotInCatalogException :
    IllegalStateException("This lego set is not in our catalog")