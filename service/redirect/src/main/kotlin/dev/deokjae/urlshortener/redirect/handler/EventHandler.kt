package dev.deokjae.urlshortener.redirect.handler

import dev.deokjae.urlshortener.common.event.Event
import dev.deokjae.urlshortener.common.event.EventPayload

interface EventHandler<T : EventPayload> {
    fun handle(event: Event<T>)
    fun supports(event: Event<T>): Boolean
}
