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
    var createdDate: Instant? = null

) {
    fun asSurveyResponse(choiceResponses: MutableList<ChoiceResponse>): SurveyResponse {
        val surveyResponse = SurveyResponse(
            choiceResponses = choiceResponses,
            submittedAt = submittedAt
        )
        surveyResponse.survey = Survey(title = "")
        surveyResponse.survey!!.uuid = UUID.fromString(surveyUuid)
        surveyResponse.createdDate = createdDate
        return surveyResponse
    }
}
