package dev.deokjae.urlshortener.shortener.dto.response

import dev.deokjae.urlshortener.shortener.entity.UrlMapping

data class UrlMappingResponse(
    val shortId: String,
    val originalUrl: String,
) {
    companion object {
        fun from(
            urlMapping: UrlMapping,
        ) = UrlMappingResponse(
            shortId = urlMapping.id!!,
            originalUrl = urlMapping.originalUrl,
        )
    }
}
