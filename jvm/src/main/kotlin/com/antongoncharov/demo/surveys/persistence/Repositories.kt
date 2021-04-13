package com.antongoncharov.demo.surveys.persistence

import com.antongoncharov.demo.surveys.model.*
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import java.util.*

/**
 * Generating CRUD REST API for given JPA entities
 */

@RepositoryRestResource(path = "users")
interface UserRepository: PagingAndSortingRepository<User, UUID> {
    fun findByEmail(email: String): User?
    fun findByName(name: String): User?
}

@RepositoryRestResource(path = "surveys")
interface SurveyRepository: PagingAndSortingRepository<Survey, UUID>

@RepositoryRestResource(path = "questions")
interface QuestionRepository: PagingAndSortingRepository<Question, UUID>

@RepositoryRestResource(path = "choices")
interface ChoiceRepository: PagingAndSortingRepository<Choice, UUID>

@RepositoryRestResource(path = "survey-responses")
interface SurveyResponseRepository: PagingAndSortingRepository<SurveyResponse, UUID>

@RepositoryRestResource(path = "choice-responses")
interface ChoiceResponseRepository: PagingAndSortingRepository<ChoiceResponse, UUID>
