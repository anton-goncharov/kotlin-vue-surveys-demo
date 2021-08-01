package com.antongoncharov.demo.surveys.model.spec

data class SearchCriteria (
    var key: String,
    var value: Any,
    var operation: SearchOperation
) {
    enum class SearchOperation {
        GREATER_THAN,
        LESS_THAN,
        GREATER_THAN_EQUAL,
        LESS_THAN_EQUAL,
        NOT_EQUAL,
        EQUAL,
        MATCH,
        MATCH_START,
        MATCH_END,
        IN,
        NOT_IN
    }
}