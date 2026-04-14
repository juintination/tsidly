package dev.deokjae.urlshortener.redirect.model

import dev.deokjae.urlshortener.common.payload.shorten.ShortIdCreatedEventPayload
import dev.deokjae.urlshortener.redirect.client.ShortenerClient

data class UrlMappingQueryModel(
    val id: String,
    val originalUrl: String,
) {
    companion object {
        fun create(
            payload: ShortIdCreatedEventPayload,
        ) = UrlMappingQueryModel(
            id = payload.shortId,
            originalUrl = payload.originalUrl,
        )

        fun create(
            response: ShortenerClient.UrlMappingResponse,
        ) = UrlMappingQueryModel(
            id = response.shortId,
            originalUrl = response.originalUrl,
        )
    }
}
