package tsidly.shortener.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tsidly.shortener.dto.response.UrlMappingResponse
import tsidly.shortener.infrastructure.redis.UrlMappingRedisWriter
import tsidly.shortener.repository.UrlMappingRepository
import java.time.Duration

@Service
class ShortenerInternalService(
    private val urlMappingRepository: UrlMappingRepository,
    private val urlMappingRedisWriter: UrlMappingRedisWriter,
) {

    @Transactional(readOnly = true)
    fun resolveOriginalUrl(
        shortId: String,
    ): UrlMappingResponse {
        val urlMapping = urlMappingRepository.findByIdOrNull(shortId)
            ?: throw NoSuchElementException("존재하지 않는 단축 URL입니다.")

        urlMappingRedisWriter.writeIfAbsent(
            urlMapping = urlMapping,
            ttl = Duration.ofHours(1),
        )

        return UrlMappingResponse.from(urlMapping)
    }
}
