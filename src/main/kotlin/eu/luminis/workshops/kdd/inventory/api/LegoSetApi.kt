package eu.luminis.workshops.kdd.inventory.api

import eu.luminis.workshops.kdd.builders.domain.BuilderId
import eu.luminis.workshops.kdd.catalog.api.toLegoSetNumber
import eu.luminis.workshops.kdd.inventory.domain.LegoSetService
import eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset.BookLegoSetCommand
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.defineLegoSetApi(legoSetService: LegoSetService) {
    routing {

        post("/api/legoset/{legoSetNumber}/book") {
            val legoSet = call.parameters["legoSetNumber"].toLegoSetNumber() ?: return@post call.respondText(
                "Missing or malformed lego set number",
                status = HttpStatusCode.BadRequest
            )
            legoSetService.handleCommand(BookLegoSetCommand(legoSet, call.loggedInBuilder()))
            call.response.status(HttpStatusCode.NoContent)
        }

    }
}

// TODO setup auth https://ktor.io/docs/authentication.html#get-principal
private fun ApplicationCall.loggedInBuilder(): BuilderId = BuilderId()
