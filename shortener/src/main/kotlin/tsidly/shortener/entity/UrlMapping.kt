package tsidly.shortener.entity

import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import tsidly.shortener.common.jpa.entity.BaseEntity

@Entity
@Table(name = "url_mappings")
@SQLRestriction("deleted_at is null")
@SQLDelete(sql = "update url_mappings set deleted_at = now() where id = ?")
class UrlMapping private constructor(

    @Id
    @Tsid
    @Column(name = "id", length = 13, nullable = false, updatable = false)
    val id: String? = null,

    @Column(nullable = false)
    val originalUrl: String,
) : BaseEntity() {

    companion object {
        fun of(
            originalUrl: String,
        ) = UrlMapping(
            originalUrl = originalUrl,
        )
    }
}
