package dev.deokjae.urlshortener.redirect.service

import dev.deokjae.urlshortener.common.event.Event
import dev.deokjae.urlshortener.common.event.EventPayload
import dev.deokjae.urlshortener.redirect.client.ShortenerClient
import dev.deokjae.urlshortener.redirect.handler.EventHandler
import dev.deokjae.urlshortener.redirect.model.UrlMappingQueryModel
import dev.deokjae.urlshortener.redirect.repository.UrlMappingQueryModelRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedirectService(
    private val urlMappingQueryModelRepository: UrlMappingQueryModelRepository,
    private val shortenerClient: ShortenerClient,
    private val eventHandlers: List<EventHandler<*>>,
) {

    private val log = KotlinLogging.logger {}

    fun handleEvent(
        event: Event<EventPayload>,
    ) {
        val handler = findHandler(
            event = event,
        )

        if (handler == null) {
            log.warn { "[RedirectService.handleEvent] No handler found for eventType=${event.type}" }
            return
        }

        handler.handle(
            event = event,
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun findHandler(
        event: Event<EventPayload>,
    ): EventHandler<EventPayload>? =
        eventHandlers
            .asSequence()
            .map { it as EventHandler<EventPayload> }
            .firstOrNull { it.supports(event) }

    fun read(
        shortId: String,
    ): String {
        val urlMappingQueryModel = urlMappingQueryModelRepository.read(
            shortId = shortId,
        ) ?: fetch(
            shortId = shortId,
        )

        return urlMappingQueryModel!!.originalUrl
    }

    private fun fetch(
        shortId: String,
    ): UrlMappingQueryModel? {
        val urlMappingQueryModel = shortenerClient.read(
            shortId = shortId,
        )?.let {
            UrlMappingQueryModel.create(
                response = it,
            )
        }

        urlMappingQueryModel?.let {
            urlMappingQueryModelRepository.create(
                urlMappingQueryModel = it,
                ttl = Duration.ofDays(1),
            )
        }

        log.info { "[RedirectService.fetch] fetch data. shortId=$shortId isPresent=${urlMappingQueryModel != null}" }

        return urlMappingQueryModel
    }
}
