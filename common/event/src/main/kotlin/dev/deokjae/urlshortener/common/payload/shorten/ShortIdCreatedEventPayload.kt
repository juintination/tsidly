package dev.deokjae.urlshortener.common.payload.shorten

import dev.deokjae.urlshortener.common.event.EventPayload

data class ShortIdCreatedEventPayload(
    val shortId: String,
    val originalUrl: String,
) : EventPayload
