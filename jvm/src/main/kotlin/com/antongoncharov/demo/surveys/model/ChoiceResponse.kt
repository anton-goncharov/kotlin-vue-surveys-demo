package com.antongoncharov.demo.surveys.model

import net.minidev.json.annotate.JsonIgnore
import javax.persistence.*

@Entity
data class ChoiceResponse(
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.DETACH])
    @JsonIgnore
    var surveyResponse: SurveyResponse? = null,

    @OneToOne(fetch = FetchType.LAZY)
    var question: Question? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "choice_uuid")
    var choice: Choice? = null
): JpaPersistable()
