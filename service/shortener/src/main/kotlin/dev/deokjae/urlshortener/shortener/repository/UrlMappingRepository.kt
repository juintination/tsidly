package dev.deokjae.urlshortener.shortener.repository

import dev.deokjae.urlshortener.shortener.entity.UrlMapping
import org.springframework.data.jpa.repository.JpaRepository

interface UrlMappingRepository : JpaRepository<UrlMapping, String>
