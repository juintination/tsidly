package dev.deokjae.urlshortener.redirect.controller

import dev.deokjae.urlshortener.redirect.service.RedirectService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/redirect")
class RedirectController(
    private val redirectService: RedirectService,
) {

    @GetMapping("/{shortId}")
    fun redirect(
        @PathVariable shortId: String,
    ): ResponseEntity<Void> {
        val url = redirectService.read(
            shortId = shortId,
        )

        return ResponseEntity
            .status(302)
            .header("Location", url)
            .build()
    }
}
