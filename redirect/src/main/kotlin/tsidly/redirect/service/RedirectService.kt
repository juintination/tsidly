package tsidly.redirect.service

import org.springframework.stereotype.Service
import tsidly.redirect.client.ShortenerClient
import tsidly.redirect.infrastructure.redis.UrlMappingRedisReader

@Service
class RedirectService(
    private val redisReader: UrlMappingRedisReader,
    private val shortenerClient: ShortenerClient,
) {

    fun resolve(
        shortId: String,
    ): String {
        redisReader.read(
            shortId = shortId,
        )?.let { originalUrl ->
            return originalUrl
        }

        val response = shortenerClient.read(
            shortId = shortId,
        ) ?: throw NoSuchElementException("존재하지 않는 단축 URL입니다.")

        return response.originalUrl
    }
}
