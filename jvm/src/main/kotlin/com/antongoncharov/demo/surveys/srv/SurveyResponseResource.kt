package com.antongoncharov.demo.surveys.srv

import com.antongoncharov.demo.surveys.model.SurveyResponse
import kotlinx.coroutines.flow.*
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller

@Controller
@MessageMapping("api.v1.messages")
class SurveyResponseResource(val surveyResponseService: SurveyResponseService) {

    @MessageMapping("stream")
    suspend fun receive(@Payload inboundSurveyResponses: Flow<SurveyResponse>) =
        surveyResponseService.post(inboundSurveyResponses)

    @MessageMapping("stream")
    fun send(surveyUuid: String): Flow<SurveyResponse> = surveyResponseService
        .stream()
        .onStart {
            emitAll(surveyResponseService.allBySurveyUuid(surveyUuid))
        }

}