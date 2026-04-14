package dev.deokjae.urlshortener.shortener.controller

import dev.deokjae.urlshortener.shortener.dto.request.UrlShortenRequest
import dev.deokjae.urlshortener.shortener.dto.response.UrlMappingResponse
import dev.deokjae.urlshortener.shortener.service.ShortenerService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/shorten")
class ShortenerController(
    private val shortenerService: ShortenerService,
) {

    @PostMapping
    fun shortenUrl(
        @RequestBody request: UrlShortenRequest,
    ): UrlMappingResponse {
        return shortenerService.shortenUrl(request)
    }
}
