package tsidly.redirect

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RedirectApplication

fun main(args: Array<String>) {
	runApplication<RedirectApplication>(*args)
}
