package dev.deokjae.urlshortener.redirect

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@EnableKafka
@SpringBootApplication
class RedirectApplication

fun main(args: Array<String>) {
    runApplication<RedirectApplication>(*args)
}
