package com.antongoncharov.demo.surveys.srv

import com.antongoncharov.demo.surveys.logger
import com.antongoncharov.demo.surveys.model.SurveyResponse
import com.antongoncharov.demo.surveys.persistence.ChoiceRepository
import com.antongoncharov.demo.surveys.persistence.ChoiceResponseRepository
import com.antongoncharov.demo.surveys.persistence.SurveyResponseRepository
import org.springframework.data.rest.core.annotation.HandleBeforeCreate
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.stereotype.Component
import java.util.*

@Component
@RepositoryEventHandler(SurveyResponse::class)
class SurveyResponseHandler(
        private val choiceRepository: ChoiceRepository
) {

    val log by logger()

    @HandleBeforeCreate
    fun handleBeforeCreate(surveyResponse: SurveyResponse) {
        log.info("surveyResponse->survey ${surveyResponse.survey?.uuid}")
        // join selected choices
        // TODO optimize
        for (choiceResponse in surveyResponse.choiceResponses) {
            val choiceUuid = choiceResponse.choice?.uuid
            if (choiceUuid != null) {
                val choice = choiceRepository.findById(choiceUuid)
                choice.ifPresent {
                    choiceResponse.choice = it
                }
            }
        }
    }

}
