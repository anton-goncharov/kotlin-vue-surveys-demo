package com.antongoncharov.demo.surveys.model.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val name: String,
    val password: String
)