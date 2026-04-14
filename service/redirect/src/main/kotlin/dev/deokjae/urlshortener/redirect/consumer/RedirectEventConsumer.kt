package dev.deokjae.urlshortener.redirect.consumer

import dev.deokjae.urlshortener.common.event.Event
import dev.deokjae.urlshortener.common.event.EventPayload
import dev.deokjae.urlshortener.common.event.Topics
import dev.deokjae.urlshortener.redirect.service.RedirectService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class RedirectEventConsumer(
    private val readService: RedirectService,
) {

    private val log = KotlinLogging.logger {}

    @KafkaListener(
        topics = [
            Topics.URL_SHORTENER,
        ]
    )
    fun listen(
        message: String,
        ack: Acknowledgment,
    ) {
        log.info { "[RedirectEventConsumer.listen] received message=$message" }

        val event: Event<EventPayload> = Event.fromJson(message)
            ?: run {
                log.warn { "Failed to deserialize event: $message" }
                ack.acknowledge()
                return
            }

        log.info { "[RedirectEventConsumer.listen] deserialized event=$event" }
        readService.handleEvent(
            event = event,
        )

        ack.acknowledge()
    }
}
