package com.antongoncharov.demo.surveys.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
data class User(
    var name: String,
    var email: String,

    @Enumerated(EnumType.STRING)
    var role: UserRole,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(updatable = false)
    var password: String? = null,

    var active: Boolean = true,

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    var preferences: UserPreferences? = null

): JpaPersistable() {

    fun asUserDetails(): UserDetails {
        return org.springframework.security.core.userdetails.User(
            email, password, active, true, true, true,
            mutableListOf(SimpleGrantedAuthority("ROLE_$role"))
        )
    }

}
