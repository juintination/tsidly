package dev.deokjae.urlshortener.common.event

import dev.deokjae.urlshortener.common.payload.shorten.ShortIdCreatedEventPayload
import io.github.oshai.kotlinlogging.KotlinLogging

private val log = KotlinLogging.logger {}

enum class EventType(
    val payloadClass: Class<out EventPayload>,
    val topic: String,
) {
    SHORT_ID_CREATED(ShortIdCreatedEventPayload::class.java, Topics.URL_SHORTENER);

    companion object {
        fun from(
            type: String,
        ): EventType? {
            return try {
                valueOf(type)
            } catch (
                e: Exception,
            ) {
                log.error(e) { "[EventType.from] type=$type" }
                null
            }
        }
    }
}
