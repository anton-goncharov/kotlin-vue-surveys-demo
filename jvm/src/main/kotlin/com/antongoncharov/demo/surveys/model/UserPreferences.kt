package com.antongoncharov.demo.surveys.model

import com.antongoncharov.demo.surveys.model.dto.UserPreferencesRequest
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
data class UserPreferences(

    @OneToOne
    @JsonIgnore
    override var user: User? = null,

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToMany
    @OrderColumn
    @JoinTable
    val mainPageLayout: MutableList<Tag> = mutableListOf(),

    var displayUntagged: Boolean = false

): JpaPersistable(), UserOwned