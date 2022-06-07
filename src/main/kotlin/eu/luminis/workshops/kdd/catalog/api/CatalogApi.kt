package eu.luminis.workshops.kdd.catalog.api

import eu.luminis.workshops.kdd.catalog.domain.readmodel.catalog.AllSetsQuery
import eu.luminis.workshops.kdd.catalog.domain.readmodel.catalog.Catalog
import eu.luminis.workshops.kdd.catalog.domain.readmodel.catalog.CatalogEntry
import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable

fun Application.defineCatalogApi(catalog: Catalog) {
    routing {

        get("/api/catalog") {
            call.respond(FindResponse(
                catalog.search(AllSetsQuery).map { Hit(it) }
            ))
        }

        get("/media/{legoSetNumber}.jpg") {
            val legoSet = call.parameters["legoSetNumber"].toLegoSetNumber() ?: return@get call.respondText(
                "Missing or malformed lego set number",
                status = HttpStatusCode.BadRequest
            )
            val entry: CatalogEntry = catalog.get(legoSet) ?: return@get call.respondText(
                "Lego set not in catalog",
                status = HttpStatusCode.NotFound
            )
            call.respondFile(entry.imageUri.toFile())
        }

    }
}

internal fun String?.toLegoSetNumber() = this?.toInt()?.let(::LegoSetNumber)

@Serializable
data class FindResponse(
    val hits: List<Hit>,
)

@Serializable
data class Hit(
    val id: Int,
    val productTitle: String,
    val imageUrl: String,
    val available: Boolean,
) {
    constructor(entry: CatalogEntry) : this(
        id = entry.id.number,
        productTitle = entry.productTitle,
        imageUrl = "media/${entry.id}.jpg",
        available = entry.assumedAmountInStock > 0,
    )
}
