package dev.deokjae.urlshortener.redirect.client

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class ShortenerClient(
    @Value("\${endpoints.url-shortener-service.url}")
    private val urlShortenerServiceUrl: String,
) {

    private val log = KotlinLogging.logger {}

    private val restClient: RestClient by lazy {
        RestClient.create(urlShortenerServiceUrl)
    }

    fun read(
        shortId: String,
    ): UrlMappingResponse? {
        return try {
            restClient.get()
                .uri("/internal/resolve/{shortId}", shortId)
                .retrieve()
                .body(UrlMappingResponse::class.java)
        } catch (
            e: Exception,
        ) {
            log.error(e) { "[ShortenerClient.read] shortId=$shortId" }
            null
        }
    }

    data class UrlMappingResponse(
        val shortId: String,
        val originalUrl: String,
    )
}
