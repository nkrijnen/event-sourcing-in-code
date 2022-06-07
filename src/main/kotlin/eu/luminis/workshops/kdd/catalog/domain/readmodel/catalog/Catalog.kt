package eu.luminis.workshops.kdd.catalog.domain.readmodel.catalog

import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber

interface Catalog {
    fun search(query: CatalogQuery): List<CatalogEntry>
    fun get(legoSet: LegoSetNumber): CatalogEntry?
}