package dev.deokjae.urlshortener.common.outbox.event

import dev.deokjae.urlshortener.common.event.Event
import dev.deokjae.urlshortener.common.event.EventPayload
import dev.deokjae.urlshortener.common.event.EventType
import dev.deokjae.urlshortener.common.outbox.entity.Outbox
import dev.deokjae.urlshortener.common.outbox.enums.MessageRelayConstants
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class OutboxEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    fun publish(
        eventType: EventType,
        payload: EventPayload,
        shardKey: Long,
    ) {
        val outbox = Outbox.create(
            eventType = eventType,
            payload = Event.of(
                type = eventType,
                payload = payload,
            ).toJson(),
            shardKey = shardKey % MessageRelayConstants.SHARD_COUNT,
        )
        applicationEventPublisher.publishEvent(
            OutboxEvent.of(
                outbox = outbox,
            )
        )
    }
}
