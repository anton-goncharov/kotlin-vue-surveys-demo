package com.antongoncharov.demo.surveys.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
data class Survey(
    @ManyToOne
    @JsonIgnore
    override var user: User? = null,

    var imageUrl: String? = null,
    var title: String,
    var active: Boolean = true,

    @OneToMany(mappedBy = "survey", cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE], orphanRemoval = true)
    @JsonIgnore
    val questions: MutableList<Question> = mutableListOf(),

    @ManyToMany
    @JoinTable
    val tags: MutableList<Tag> = mutableListOf(),

    @OneToMany(mappedBy = "survey", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    @JsonIgnore
    val surveyResponses: MutableList<SurveyResponse> = mutableListOf()

): JpaPersistable(), UserOwned