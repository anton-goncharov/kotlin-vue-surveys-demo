package com.antongoncharov.demo.surveys.srv

import com.antongoncharov.demo.surveys.logger
import com.antongoncharov.demo.surveys.model.SurveyResponse
import com.antongoncharov.demo.surveys.persistence.ChoiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.springframework.data.rest.core.annotation.HandleBeforeCreate
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.stereotype.Component

@Component
@RepositoryEventHandler(SurveyResponse::class)
class SurveyResponseHandler(
        private val choiceRepository: ChoiceRepository,
        private val surveyResponseRxService: SurveyResponseRxService
) {

    val log by logger()

    @HandleBeforeCreate
    fun handleBeforeCreate(surveyResponse: SurveyResponse) {
        log.info("surveyResponse->survey ${surveyResponse.survey?.uuid}")
        // join selected choices
        // TODO optimize or eliminate
        for (choiceResponse in surveyResponse.choiceResponses) {
            val choiceUuid = choiceResponse.choice?.uuid
            if (choiceUuid != null) {
                val choice = choiceRepository.findById(choiceUuid)
                choice.ifPresent {
                    choiceResponse.choice = it
                }
            }
        }
        // emit reactive flow
        runBlocking {
            log.info("emit(${surveyResponse.uuid})")
            surveyResponseRxService.post(flow { emit(surveyResponse) })
        }
    }

}
