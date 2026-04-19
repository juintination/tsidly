package tsidly.redirect.infrastructure.redis

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

@Component
class UrlMappingRedisReader(
    private val redisTemplate: StringRedisTemplate,
) {

    companion object {
        private const val KEY_FORMAT = "tsidly:short-id:%s"
    }

    fun read(
        shortId: String,
    ): String? {
        return redisTemplate.opsForValue()
            .get(generateKey(shortId))
    }

    private fun generateKey(
        shortId: String,
    ) = KEY_FORMAT.format(shortId)
}
