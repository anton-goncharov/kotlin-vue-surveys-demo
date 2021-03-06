package com.antongoncharov.demo.surveys.model

import org.springframework.data.rest.core.config.Projection
import java.time.Instant
import java.util.*

@Projection(name = "surveyWithQuestions", types = [Survey::class])
interface SurveyWithQuestions {
    val uuid: UUID
    val title: String
    val imageUrl: String?
    val active: Boolean
    val tags: MutableList<Tag>
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


@Projection(name = "surveyResponseBrief", types = [SurveyResponse::class])
interface SurveyResponseBrief {
    val id: UUID
    val submitted: Boolean
    val submittedAt: Instant
    val choiceResponses: MutableList<ChoiceResponseBrief>
}

@Projection(name = "choiceResponseBrief", types = [ChoiceResponse::class])
interface ChoiceResponseBrief {
    val question: QuestionBrief
    val choice: ChoiceBrief
}

@Projection(name = "choiceBrief", types = [Choice::class])
interface ChoiceBrief {
    val id: UUID
}

@Projection(name = "questionBrief", types = [Question::class])
interface QuestionBrief {
    val id: UUID
}
