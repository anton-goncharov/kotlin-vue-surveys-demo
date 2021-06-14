package com.antongoncharov.demo.surveys.api

import com.antongoncharov.demo.surveys.logger
import com.antongoncharov.demo.surveys.model.SurveyResponse
import com.antongoncharov.demo.surveys.model.SurveyResponseBrief
import com.antongoncharov.demo.surveys.srv.SurveyResponseService
import kotlinx.coroutines.flow.Flow
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

// TODO remove this class
@RestController
class SurveyResponseController (
    private val surveyResponseService: SurveyResponseService
) {

    val LOG by logger()

    @GetMapping("flow", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun flow(@RequestParam("surveyUuid") surveyUuid: String): Flow<SurveyResponse> {
        LOG.info("getting choice response flow")
        return surveyResponseService.allBySurveyUuid(surveyUuid)
    }

}