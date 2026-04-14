package dev.deokjae.urlshortener.shortener.service

import dev.deokjae.urlshortener.common.event.EventType
import dev.deokjae.urlshortener.common.outbox.event.OutboxEventPublisher
import dev.deokjae.urlshortener.common.payload.shorten.ShortIdCreatedEventPayload
import dev.deokjae.urlshortener.shortener.dto.request.UrlShortenRequest
import dev.deokjae.urlshortener.shortener.dto.response.UrlMappingResponse
import dev.deokjae.urlshortener.shortener.entity.UrlMapping
import dev.deokjae.urlshortener.shortener.repository.UrlMappingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ShortenerService(
    private val urlMappingRepository: UrlMappingRepository,
    private val outboxEventPublisher: OutboxEventPublisher,
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

        outboxEventPublisher.publish(
            eventType = EventType.SHORT_ID_CREATED,
            payload = ShortIdCreatedEventPayload(
                shortId = urlMapping.id!!,
                originalUrl = urlMapping.originalUrl,
            ),
            shardKey = urlMapping.id.hashCode().toLong(),
        )

        return UrlMappingResponse.from(urlMapping)
    }
}
