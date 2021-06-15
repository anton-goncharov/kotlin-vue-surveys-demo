package com.antongoncharov.demo.surveys.model

import com.antongoncharov.demo.surveys.model.r2dbc.ChoiceResponseRow
import com.antongoncharov.demo.surveys.model.r2dbc.SurveyResponseRow
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
): JpaPersistable(), UserOwned {

    fun asRelational(): SurveyResponseRow {
        val surveyResponseRow = SurveyResponseRow()
        surveyResponseRow.uuid = this.uuid.toString()
        surveyResponseRow.surveyUuid = this.survey?.uuid.toString()
        surveyResponseRow.submittedAt = this.submittedAt
        surveyResponseRow.createdDate = this.createdDate

        for (choiceResponse in choiceResponses) {
            val choiceResponseRow = ChoiceResponseRow()
            choiceResponseRow.uuid = choiceResponse.uuid.toString()
            choiceResponseRow.surveyResponseUuid = choiceResponse.surveyResponse?.uuid.toString()
            choiceResponseRow.choiceUuid = choiceResponse.choice?.uuid.toString()
            choiceResponseRow.questionUuid = choiceResponse.question?.uuid.toString()
            surveyResponseRow.choiceResponses.add(choiceResponseRow)
        }
        return surveyResponseRow
    }

}