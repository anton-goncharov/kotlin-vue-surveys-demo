package com.antongoncharov.demo.surveys.model

import org.springframework.data.rest.core.config.Projection
import java.util.*

@Projection(name = "surveyWithQuestions", types = [Survey::class])
interface SurveyWithQuestions {
    val uuid: UUID
    val title: String
    val active: Boolean
    val questions: MutableList<QuestionWithChoices>
}

@Projection(name = "questionWithChoices", types = [Question::class])
interface QuestionWithChoices {
    val uuid: UUID
    val text: String
    val pos: Int
    val multiselect: Boolean
    val active: Boolean
    val choices: MutableList<Choice>
}