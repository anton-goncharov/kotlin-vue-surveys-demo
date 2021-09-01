package com.antongoncharov.demo.surveys.model.dto

data class UserPreferencesRequest (
    val mainPageLayout: List<String> = mutableListOf(),
    val displayUntagged: Boolean = false
)