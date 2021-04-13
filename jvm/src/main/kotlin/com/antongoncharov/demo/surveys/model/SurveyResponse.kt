package com.antongoncharov.demo.surveys.model

import java.time.Instant
import javax.persistence.*

@Entity
data class SurveyResponse(
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.DETACH])
    var survey: Survey? = null,

    @ManyToOne
    override var user: User? = null,

    var submitted: Boolean = false,
    var submittedAt: Instant? = null,

    @OneToMany(mappedBy = "surveyResponse", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val choiceResponses: MutableList<ChoiceResponse> = mutableListOf()
): JpaPersistable(), UserOwned
