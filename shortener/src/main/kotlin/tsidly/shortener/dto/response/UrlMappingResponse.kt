package tsidly.shortener.dto.response

import tsidly.shortener.entity.UrlMapping

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
