package com.antongoncharov.demo.surveys.persistence

import com.antongoncharov.demo.surveys.model.*
import org.springframework.data.jpa.repository.Query
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

@RepositoryRestResource(path = "choices", exported = false)
interface ChoiceRepository: PagingAndSortingRepository<Choice, UUID>

@RepositoryRestResource(path = "survey-responses")
interface SurveyResponseRepository: PagingAndSortingRepository<SurveyResponse, UUID> {

    fun findByUuid(uuid: UUID?): Optional<SurveyResponseBrief>

    fun findAllByUser(user: User): List<SurveyResponse>

    @Query("SELECT sr FROM SurveyResponse sr WHERE sr.survey.id = ?1 AND sr.user.id = :#{@requestContext.CurrentUser.user.id}")
    fun findBySurveyForCurrentUser(surveyUuid: UUID): SurveyResponse?
}

@RepositoryRestResource(path = "choice-responses", exported = false)
interface ChoiceResponseRepository: PagingAndSortingRepository<ChoiceResponse, UUID>
