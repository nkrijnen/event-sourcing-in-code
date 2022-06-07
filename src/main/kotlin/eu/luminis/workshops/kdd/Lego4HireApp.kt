package eu.luminis.workshops.kdd

import eu.luminis.workshops.kdd.catalog.CatalogContext
import eu.luminis.workshops.kdd.inventory.InventoryContext
import eu.luminis.workshops.kdd.inventory.domain.writemodel.legoset.NotInCatalogException
import eu.luminis.workshops.kdd.system.IntegrationEvent
import eu.luminis.workshops.kdd.system.eventbus.EventBus
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(
        Netty,
        port = 8015,
        host = "127.0.0.1",
        module = Application::appModule
    ).start(wait = true)
}

fun Application.appModule() {
    configureServer()
    registerExceptionTranslations()
    instantiateServices()
}

private fun Application.instantiateServices() {
    val eventBus = EventBus<IntegrationEvent>()

    CatalogContext(this, eventBus)

    InventoryContext(this, eventBus)
}

private fun Application.configureServer() {
    install(ContentNegotiation) {
        json()
    }
    install(DefaultHeaders)
    install(CallLogging)
}

private fun Application.registerExceptionTranslations() {
    install(StatusPages) {
        exception<NotInCatalogException> { cause ->
            call.respondText(
                cause.message ?: "Not found",
                status = HttpStatusCode.NotFound
            )
        }
        exception<IllegalArgumentException> { cause ->
            call.respondText(
                cause.message ?: "Malformed request",
                status = HttpStatusCode.BadRequest
            )
        }
        exception<IllegalStateException> { cause ->
            call.respondText(
                cause.message ?: "Not possible in current state",
                status = HttpStatusCode.Conflict
            )
        }
    }
}
