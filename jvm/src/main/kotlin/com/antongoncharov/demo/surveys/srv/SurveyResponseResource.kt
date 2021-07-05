package com.antongoncharov.demo.surveys.srv

import com.antongoncharov.demo.surveys.logger
import com.antongoncharov.demo.surveys.model.SurveyResponse
import com.antongoncharov.demo.surveys.model.SurveyResponseBrief
import kotlinx.coroutines.flow.*
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller

@Controller
@MessageMapping("api.v1.survey-response")
class SurveyResponseResource(val surveyResponseRxService: SurveyResponseRxService) {

    data class SurveyStatRequest(val surveyUuid: String)

    val log by logger()

    @MessageMapping("stream")
    suspend fun receive(@Payload inboundSurveyResponses: Flow<SurveyResponse>) =
        surveyResponseRxService.post(inboundSurveyResponses)

    @MessageMapping("stream")
    suspend fun send(req: SurveyStatRequest): Flow<SurveyResponseBrief> {
        val surveyUuid = req.surveyUuid
        return surveyResponseRxService
            .stream(surveyUuid)
            .onStart {
                emitAll(surveyResponseRxService.findAllSubmittedBySurveyUuid(surveyUuid))
            }
    }

}