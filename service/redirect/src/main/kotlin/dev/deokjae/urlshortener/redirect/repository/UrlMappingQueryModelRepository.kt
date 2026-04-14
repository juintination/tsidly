package dev.deokjae.urlshortener.redirect.repository

import dev.deokjae.urlshortener.common.serialization.DataSerializer
import dev.deokjae.urlshortener.redirect.model.UrlMappingQueryModel
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class UrlMappingQueryModelRepository(
    private val redisTemplate: StringRedisTemplate,
) {

    companion object {
        private const val KEY_FORMAT = "url-shortener:mapping:%s"
    }

    private val log = KotlinLogging.logger {}

    fun create(
        urlMappingQueryModel: UrlMappingQueryModel,
        ttl: Duration,
    ) {
        val key = generateKey(
            urlMappingQueryModel = urlMappingQueryModel,
        )

        redisTemplate.opsForValue()
            .set(key, DataSerializer.toJson(urlMappingQueryModel), ttl)
    }

    fun update(
        urlMappingQueryModel: UrlMappingQueryModel,
    ) {
        val key = generateKey(
            urlMappingQueryModel = urlMappingQueryModel,
        )

        redisTemplate.opsForValue()
            .setIfPresent(key, DataSerializer.toJson(urlMappingQueryModel))
    }

    fun delete(
        shortId: String,
    ) {
        val key = generateKey(
            shortId = shortId,
        )

        redisTemplate.delete(key)
    }

    fun read(
        shortId: String,
    ): UrlMappingQueryModel? {
        val key = generateKey(
            shortId = shortId,
        )

        val json = redisTemplate.opsForValue().get(key) ?: return null
        return try {
            DataSerializer.fromJson<UrlMappingQueryModel>(json)
        } catch (e: Exception) {
            log.error(e) { "[UrlMappingQueryModelRepository.read] shortId=$shortId" }
            null
        }
    }

    private fun generateKey(
        urlMappingQueryModel: UrlMappingQueryModel,
    ) = generateKey(urlMappingQueryModel.id)

    private fun generateKey(
        shortId: String,
    ) = KEY_FORMAT.format(shortId)
}
