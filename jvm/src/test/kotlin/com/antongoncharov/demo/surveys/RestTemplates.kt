package com.antongoncharov.demo.surveys

import com.antongoncharov.demo.surveys.security.JwtTokenUtil
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.core.userdetails.User

private val TEST_USERNAME = "testuser@mail.test"

fun authorizedHttpEntity(jwtTokenUtil: JwtTokenUtil): HttpEntity<Any> {
    val httpEntity = HttpEntity<Any>(authHttpHeaders(jwtTokenUtil))
    return httpEntity
}

fun <T> authorizedHttpEntity(jwtTokenUtil: JwtTokenUtil, value: T): HttpEntity<T> {
    val httpEntity = HttpEntity<T>(value, authHttpHeaders(jwtTokenUtil))
    return httpEntity
}

private fun authHttpHeaders(jwtTokenUtil: JwtTokenUtil): HttpHeaders {
    val token = jwtTokenUtil.generateToken(userDetails = User(TEST_USERNAME, "", listOf()));
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_JSON
    headers.accept = listOf(MediaType.APPLICATION_JSON)
    headers.add("Authorization", "Bearer $token")
    return headers
}