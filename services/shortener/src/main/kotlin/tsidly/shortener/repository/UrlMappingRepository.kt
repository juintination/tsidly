package tsidly.shortener.repository

import org.springframework.data.jpa.repository.JpaRepository
import tsidly.shortener.entity.UrlMapping

interface UrlMappingRepository : JpaRepository<UrlMapping, String> {
    fun findByOriginalUrl(originalUrl: String): UrlMapping?
}
