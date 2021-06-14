package com.antongoncharov.demo.surveys.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.Instant
import java.util.*
import javax.persistence.*

@Entity
data class SurveyResponse(
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.DETACH])
    @JsonIgnore
    var survey: Survey? = null,

    @ManyToOne
    override var user: User? = null,

    var submitted: Boolean = false,
    var submittedAt: Instant? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "survey_response_uuid")
    val choiceResponses: MutableList<ChoiceResponse> = mutableListOf()
): JpaPersistable(), UserOwned