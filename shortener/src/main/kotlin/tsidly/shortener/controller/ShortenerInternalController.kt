package tsidly.shortener.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tsidly.shortener.dto.response.UrlMappingResponse
import tsidly.shortener.service.ShortenerInternalService

@RestController
@RequestMapping("/internal/resolve")
class ShortenerInternalController(
    private val shortenerInternalService: ShortenerInternalService,
) {

    @GetMapping("/{shortId}")
    fun resolveOriginalUrl(
        @PathVariable shortId: String,
    ): UrlMappingResponse {
        return shortenerInternalService.resolveOriginalUrl(shortId)
    }
}
