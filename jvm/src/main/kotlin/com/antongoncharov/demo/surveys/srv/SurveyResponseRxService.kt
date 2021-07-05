package com.antongoncharov.demo.surveys.srv

import com.antongoncharov.demo.surveys.logger
import com.antongoncharov.demo.surveys.model.SurveyResponse
import com.antongoncharov.demo.surveys.model.SurveyResponseBrief
import com.antongoncharov.demo.surveys.model.r2dbc.SurveyResponseRow
import com.antongoncharov.demo.surveys.persistence.SurveyResponseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.asFlow
import liquibase.pro.packaged.c
import org.springframework.data.domain.Sort.Order.desc
import org.springframework.data.domain.Sort.by
import org.springframework.data.projection.ProjectionFactory
import org.springframework.data.projection.SpelAwareProxyProjectionFactory
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class SurveyResponseRxService(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate,
    private val surveyResponseRepository: SurveyResponseRepository
) {

    val log by logger()
    val pf: ProjectionFactory = SpelAwareProxyProjectionFactory()

    private val surveyFlows: MutableMap<String, MutableSharedFlow<SurveyResponseBrief>> = mutableMapOf()

//    val sender: MutableSharedFlow<SurveyResponseBrief> = MutableSharedFlow()

    fun stream(surveyUuid: String): MutableSharedFlow<SurveyResponseBrief> {
        return surveyFlows.getOrPut(surveyUuid, { MutableSharedFlow() })
    }

    /**
     * Returns all existing survey responses
     */
    suspend fun findAll(): Flow<SurveyResponseBrief> = r2dbcEntityTemplate
        .select(
            Query.empty().sort(by(desc("created_date"))),
            SurveyResponseRow::class.java
        )
        .map {
            val brief = surveyResponseRepository.findBriefById(UUID.fromString(it.uuid!!))
            brief
        }
        .asFlow()


    /**
     * Returns all existing survey responses for the given survey uuid
     */
    suspend fun findAllSubmittedBySurveyUuid(surveyUuid: String): Flow<SurveyResponseBrief> = r2dbcEntityTemplate
        .select(
            Query.query(
                where("survey_uuid").`is`(surveyUuid)
                    .and("submitted").`is`(true)
            ).sort(by(desc("created_date"))),
            SurveyResponseRow::class.java
        )
        .map {
            val brief = surveyResponseRepository.findBriefById(UUID.fromString(it.uuid!!))
            brief
        }
        .asFlow()

    suspend fun post(responses: Flow<SurveyResponse>) =
        responses.collect {
                // TODO avoid '!!'
                val surveyUuid = it.survey!!.uuid.toString()
                val brief = pf.createProjection(SurveyResponseBrief::class.java, it)
                stream(surveyUuid).emit(brief)
            }
}