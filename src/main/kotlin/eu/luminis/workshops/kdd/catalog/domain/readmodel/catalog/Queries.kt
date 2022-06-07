package eu.luminis.workshops.kdd.catalog.domain.readmodel.catalog

import eu.luminis.workshops.kdd.system.Query

sealed interface CatalogQuery : Query<List<CatalogEntry>>
object AllSetsQuery : CatalogQuery
object PopularQuery : CatalogQuery