package dev.deokjae.urlshortener.common.outbox.event

import dev.deokjae.urlshortener.common.event.EventType
import dev.deokjae.urlshortener.common.outbox.entity.Outbox

class OutboxEvent private constructor(
    val eventType: EventType,
    val payload: String,
    val shardKey: Long,
) {
    companion object {
        fun of(
            outbox: Outbox,
        ) = OutboxEvent(
            eventType = outbox.eventType,
            payload = outbox.payload,
            shardKey = outbox.shardKey,
        )
    }
}
