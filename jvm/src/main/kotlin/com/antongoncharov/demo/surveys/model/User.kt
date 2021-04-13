package com.antongoncharov.demo.surveys.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.jpa.domain.AbstractAuditable
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.persistence.Column

import javax.persistence.Entity

@Entity
data class User(
    var name: String,
    var email: String,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(updatable = false)
    var password: String? = null, // $2a$10$WsfNNx6ISzILD5ey2Us2keXXrcjoucB23HpQmzbep38xKNMw4/uA6

    var active: Boolean = true
): JpaPersistable() {

    fun asUserDetails(): UserDetails {
        return org.springframework.security.core.userdetails.User(
            email, password, active, true, true, true, mutableListOf())
    }
}
