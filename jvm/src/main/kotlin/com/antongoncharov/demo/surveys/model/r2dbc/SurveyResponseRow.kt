package com.antongoncharov.demo.surveys.model.r2dbc

import com.antongoncharov.demo.surveys.model.ChoiceResponse
import com.antongoncharov.demo.surveys.model.Survey
import com.antongoncharov.demo.surveys.model.SurveyResponse
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("SURVEY_RESPONSE")
data class SurveyResponseRow (

    var uuid: String? = null,
    var surveyUuid: String? = null,
    var submittedAt: Instant? = null,
    var createdDate: Instant? = null,

    @Transient
    var choiceResponses: MutableList<ChoiceResponseRow> = mutableListOf()
)
