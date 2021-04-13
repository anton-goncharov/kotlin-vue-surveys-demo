package com.antongoncharov.demo.surveys.model

import javax.persistence.*

@Entity
data class ChoiceResponse(
    @ManyToOne(fetch = FetchType.LAZY)
    var surveyResponse: SurveyResponse? = null,

    @OneToOne(fetch = FetchType.LAZY)
    var question: Question? = null,

    @OneToOne(fetch = FetchType.LAZY)
    var choice: Choice? = null
): JpaPersistable()
