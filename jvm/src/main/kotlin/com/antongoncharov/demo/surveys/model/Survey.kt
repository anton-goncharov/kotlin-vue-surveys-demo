package com.antongoncharov.demo.surveys.model

import javax.persistence.*

@Entity
data class Survey(
    @ManyToOne
    override var user: User? = null,

    var imageUrl: String? = null,
    var title: String,
    var active: Boolean = true,

    @OneToMany(mappedBy = "survey", cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE], orphanRemoval = true)
    val questions: MutableList<Question> = mutableListOf(),

    @ManyToMany
    @JoinTable
//        joinColumns = [JoinColumn(name = "id")],
//        inverseJoinColumns = [JoinColumn(name = "id")])
    val tags: MutableList<Tag> = mutableListOf(),

    @OneToMany(mappedBy = "survey", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val surveyResponses: MutableList<SurveyResponse> = mutableListOf()
): JpaPersistable(), UserOwned
