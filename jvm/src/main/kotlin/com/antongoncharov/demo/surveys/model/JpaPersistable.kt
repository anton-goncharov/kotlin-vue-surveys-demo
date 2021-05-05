package com.antongoncharov.demo.surveys.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.Type
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.AbstractAuditable
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*
import javax.persistence.*

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
abstract class JpaPersistable(givenId: UUID? = null) {

    @Id
    @Column(name = "uuid")
    @JsonIgnore
    @GeneratedValue
    @Type(type="uuid-char")
    var id: UUID = givenId ?: UUID.randomUUID()

    // existence of a @Version value to tell if it's persisted or not.
    // by having @Version, we also get optimistic locking for free
    @Version
    var version: Long? = null

    // spring data rest hides @Id field value from JSON, so we're exposing it as "uuid" delegate field
    var uuid: UUID
        get() = id
        set(id) { this.id = id }

//    @ManyToOne
//    var createdBy: User? = null
//
//    @ManyToOne
//    var lastModifiedBy: User? = null
//
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    var createdDate: Instant? = null

    @LastModifiedDate
    @Column(name = "last_modified_date")
    var lastModifiedDate: Instant? = null

    override fun hashCode(): Int = uuid.hashCode()

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other == null -> false
            other !is JpaPersistable -> false
            else -> uuid == other.uuid
        }
    }

}
