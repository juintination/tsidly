package tsidly.shortener.infrastructure.redis

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import tsidly.shortener.entity.UrlMapping
import java.time.Duration

@Component
class UrlMappingRedisWriter(
    private val redisTemplate: StringRedisTemplate,
) {

    companion object {
        private const val KEY_FORMAT = "tsidly:short-id:%s"
    }

    fun write(
        urlMapping: UrlMapping,
        ttl: Duration,
    ) {
        val key = generateKey(
            shortId = urlMapping.id!!,
        )

        redisTemplate.opsForValue()
            .set(key, urlMapping.originalUrl, ttl)
    }

    private fun generateKey(
        shortId: String,
    ) = KEY_FORMAT.format(shortId)
}
