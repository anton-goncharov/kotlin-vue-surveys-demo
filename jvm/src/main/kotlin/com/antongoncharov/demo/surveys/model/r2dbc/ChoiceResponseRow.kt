package com.antongoncharov.demo.surveys.model.r2dbc

data class ChoiceResponseRow(
    var uuid: String? = null,
    var surveyResponseUuid: String? = null,
    var questionUuid: String? = null,
    var choiceUuid: String? = null
)
