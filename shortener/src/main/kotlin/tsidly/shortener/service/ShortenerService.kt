package tsidly.shortener.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tsidly.shortener.dto.request.UrlShortenRequest
import tsidly.shortener.dto.response.UrlMappingResponse
import tsidly.shortener.entity.UrlMapping
import tsidly.shortener.infrastructure.redis.UrlMappingRedisWriter
import tsidly.shortener.repository.UrlMappingRepository
import java.time.Duration

@Service
class ShortenerService(
    private val urlMappingRepository: UrlMappingRepository,
    private val urlMappingRedisWriter: UrlMappingRedisWriter,
) {

    @Transactional
    fun shortenUrl(
        request: UrlShortenRequest,
    ): UrlMappingResponse {
        val urlMapping = urlMappingRepository.save(
            UrlMapping.of(
                originalUrl = request.originalUrl,
            )
        )

        urlMappingRedisWriter.write(
            urlMapping = urlMapping,
            ttl = Duration.ofHours(1),
        )

        return UrlMappingResponse.from(urlMapping)
    }
}
