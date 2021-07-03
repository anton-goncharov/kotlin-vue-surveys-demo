package com.antongoncharov.demo.surveys.model.r2dbc

import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("SURVEYRESPONSE")
data class SurveyResponseRow (

    var uuid: String? = null,
    var surveyUuid: String? = null,
    var submittedAt: Instant? = null,
    var createdDate: Instant? = null,

    @Transient
    var choiceResponses: MutableList<ChoiceResponseRow> = mutableListOf()
)
