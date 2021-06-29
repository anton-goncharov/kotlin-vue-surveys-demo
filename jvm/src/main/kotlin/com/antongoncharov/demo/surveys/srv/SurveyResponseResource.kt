package com.antongoncharov.demo.surveys.srv

import com.antongoncharov.demo.surveys.model.SurveyResponse
import com.antongoncharov.demo.surveys.model.SurveyResponseBrief
import kotlinx.coroutines.flow.*
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller

@Controller
@MessageMapping("api.v1.survey-response")
class SurveyResponseResource(val surveyResponseRxService: SurveyResponseRxService) {

    @MessageMapping("stream")
    suspend fun receive(@Payload inboundSurveyResponses: Flow<SurveyResponse>) =
        surveyResponseRxService.post(inboundSurveyResponses)

    @MessageMapping("stream")
    fun send(): Flow<SurveyResponseBrief> = surveyResponseRxService
        .stream()
        .onStart {
            emitAll(surveyResponseRxService.findAll())
        }

}