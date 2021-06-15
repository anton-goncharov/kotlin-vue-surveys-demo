package com.antongoncharov.demo.surveys.srv

import com.antongoncharov.demo.surveys.model.SurveyResponse
import com.antongoncharov.demo.surveys.model.r2dbc.SurveyResponseRow
import com.antongoncharov.demo.surveys.persistence.ChoiceResponseRepository
import com.antongoncharov.demo.surveys.persistence.SurveyResponseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.asFlow
import org.springframework.data.domain.Sort.Order.desc
import org.springframework.data.domain.Sort.by
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Service
import java.util.*

@Service
class SurveyResponseService(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate,
    private val surveyResponseRepository: SurveyResponseRepository
) {

    val sender: MutableSharedFlow<SurveyResponse> = MutableSharedFlow()

    fun stream(): Flow<SurveyResponse> = sender

    /**
     * Returns all existing survey responses for the given survey uuid
     */
    suspend fun allBySurveyUuid(surveyUuid: String): Flow<SurveyResponse> = r2dbcEntityTemplate
        .select(
            Query.query(
                where("survey_uuid").`is`(surveyUuid))
                .sort(by(desc("created_date"))),
            SurveyResponseRow::class.java
        )
        .map {
            // TODO optimize number of subqueries
            val surveyResponse = surveyResponseRepository.findById(UUID.fromString(it.uuid!!))
            surveyResponse.get()
        }
        .asFlow()

    suspend fun post(responses: Flow<SurveyResponse>) =
        responses
            .onEach { sender.emit(it) }
                // map to "a SurveyResponseRow + a list of ChoiceResponseRows"
            .map { it.asRelational() }
                // it can only insert a row
            .let {
                r2dbcEntityTemplate.insert(it)
                it.onEach {
                    r2dbcEntityTemplate.insert(it.choiceResponses.asFlow())
                }
            }
}