package tsidly.shortener.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tsidly.shortener.dto.response.UrlMappingResponse
import tsidly.shortener.repository.UrlMappingRepository

@Service
class ShortenerInternalService(
    private val urlMappingRepository: UrlMappingRepository,
) {

    @Transactional(readOnly = true)
    fun resolveOriginalUrl(
        shortId: String,
    ): UrlMappingResponse {
        val urlMapping = urlMappingRepository.findByIdOrNull(shortId)
            ?: throw NoSuchElementException("존재하지 않는 단축 URL입니다.")

        return UrlMappingResponse.from(urlMapping)
    }
}
