package dev.deokjae.urlshortener.shortener

import dev.deokjae.urlshortener.common.jpa.config.JpaAuditConfig
import dev.deokjae.urlshortener.common.outbox.config.MessageRelayConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EntityScan(
    "dev.deokjae.urlshortener.shortener.entity",
    "dev.deokjae.urlshortener.common.outbox.entity",
)
@EnableJpaRepositories(
    "dev.deokjae.urlshortener.shortener.repository",
    "dev.deokjae.urlshortener.common.outbox.repository",
)
@Import(
    JpaAuditConfig::class,
    MessageRelayConfig::class,
)
@SpringBootApplication
class ShortenerApplication

fun main(args: Array<String>) {
    runApplication<ShortenerApplication>(*args)
}
