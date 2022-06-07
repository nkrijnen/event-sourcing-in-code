package eu.luminis.workshops.kdd;

import eu.luminis.workshops.kdd.catalog.api.FindResponse
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class Lego4HireAppTest {

    @Test
    fun `should return all lego sets in catalog`() {
        withTestApplication({ appModule() }) {
            handleRequest(HttpMethod.Get, "/api/catalog").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
                val findResponse = Json.decodeFromString<FindResponse>(response.content ?: "")
                assertThat(findResponse.hits).hasSize(42)
                assertThat(findResponse.hits.find { it.productTitle == "The Batman Batmobile" }?.imageUrl)
                    .isEqualTo("media/42127.jpg")
            }
        }
    }

    @Test
    fun `should return image for lego set`() {
        withTestApplication({ appModule() }) {
            handleRequest(HttpMethod.Get, "/media/42127.jpg").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
                assertThat(response.headers["Content-Type"]).isEqualTo("image/jpeg")
                assertThat(response.headers["Content-Length"]).isEqualTo("11179")
            }
        }
    }

    @Test
    fun `should book set`() {
        withTestApplication({ appModule() }) {
            handleRequest(HttpMethod.Post, "/api/legoset/10294/book").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.NoContent)
                assertThat(response.content).isNull()
            }
        }
    }

    @Test
    fun `should fail to book set that is not in catalog`() {
        withTestApplication({ appModule() }) {
            handleRequest(HttpMethod.Post, "/api/legoset/999/book").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.NotFound)
                assertThat(response.content).isEqualTo("This lego set is not in our catalog")
            }
        }
    }

    @Test
    fun `should fail to book set that is not available`() {
        withTestApplication({ appModule() }) {
            handleRequest(HttpMethod.Post, "/api/legoset/42127/book").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.Conflict)
                assertThat(response.content).isEqualTo("Lego set 42127 is not available at this moment")
            }
        }
    }
}