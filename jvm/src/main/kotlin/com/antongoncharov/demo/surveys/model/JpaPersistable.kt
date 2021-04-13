package com.antongoncharov.demo.surveys.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.AbstractAuditable
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*
import javax.persistence.*

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class JpaPersistable(givenId: UUID? = null) {

    @Id
    @Column(name = "uuid")
    @JsonIgnore
    @GeneratedValue
    var id: UUID = givenId ?: UUID.randomUUID()

    // existence of a @Version value to tell if it's persisted or not.
    // by having @Version, we also get optimistic locking for free
    @Version
    var version: Long? = null

    val uuid: UUID
        get() = id

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
