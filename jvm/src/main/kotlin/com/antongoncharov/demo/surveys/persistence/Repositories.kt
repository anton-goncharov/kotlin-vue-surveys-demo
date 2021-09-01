package com.antongoncharov.demo.surveys.persistence

import com.antongoncharov.demo.surveys.model.*
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.access.prepost.PreAuthorize
import java.time.Instant
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
interface SurveyRepository: PagingAndSortingRepository<Survey, UUID>, ProtectedRepository<Survey>, JpaSpecificationExecutor<Survey> {

    @Query(value = "SELECT s FROM Survey s where s.createdDate BETWEEN :startDate AND :endDate")
    fun getAllSurveysBetweenDates(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Param("startDate") startDate: Instant,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Param("endDate") endDate: Instant
    ): List<Survey>

    override fun findAll(spec: Specification<Survey>?): MutableList<Survey>
}

@RepositoryRestResource(path = "questions")
interface QuestionRepository: PagingAndSortingRepository<Question, UUID>, ProtectedRepository<Question>

@RepositoryRestResource(path = "tags")
interface TagRepository: PagingAndSortingRepository<Tag, UUID> {
    fun findByShortName(shortName: String): Tag?
    fun findByShortNameIn(shortName: List<String>): List<Tag>
}

@RepositoryRestResource(path = "preferences", exported = false)
interface UserPreferencesRepository: PagingAndSortingRepository<UserPreferences, UUID> {
    @Query("SELECT up FROM UserPreferences up WHERE up.user.id = :#{@requestContext.CurrentUser.user.id}")
    fun findForCurrentUser(): UserPreferences?
}

@RepositoryRestResource(path = "choices", exported = false)
interface ChoiceRepository: PagingAndSortingRepository<Choice, UUID>, ProtectedRepository<Choice>

@RepositoryRestResource(path = "survey-responses")
interface SurveyResponseRepository: PagingAndSortingRepository<SurveyResponse, UUID>, DeleteNotExportedRepository<SurveyResponse> {

    fun findBriefById(uuid: UUID?): SurveyResponseBrief

    fun findAllByUser(user: User): List<SurveyResponse>

    @Query("SELECT sr FROM SurveyResponse sr WHERE sr.survey.id = ?1 AND sr.user.id = :#{@requestContext.CurrentUser.user.id}")
    fun findBySurveyForCurrentUser(surveyUuid: UUID): SurveyResponse?
}

@RepositoryRestResource(path = "choice-responses", exported = false)
interface ChoiceResponseRepository: PagingAndSortingRepository<ChoiceResponse, UUID>, DeleteNotExportedRepository<ChoiceResponse>

interface ProtectedRepository<T>: PagingAndSortingRepository<T, UUID> {
    @PreAuthorize("hasRole('COORDINATOR')")
    override fun <S : T?> save(entity: S): S
    @PreAuthorize("hasRole('COORDINATOR')")
    override fun <S : T?> saveAll(entities: MutableIterable<S>): MutableIterable<S>
    @PreAuthorize("hasRole('COORDINATOR')")
    override fun deleteById(id: UUID)
    @PreAuthorize("hasRole('COORDINATOR')")
    override fun delete(entity: T)
    @PreAuthorize("hasRole('COORDINATOR')")
    override fun deleteAll(entities: MutableIterable<T>)
    @PreAuthorize("hasRole('COORDINATOR')")
    override fun deleteAll()
}

interface DeleteNotExportedRepository<T>: PagingAndSortingRepository<T, UUID> {
    @RestResource(exported = false)
    override fun deleteById(id: UUID)
    @RestResource(exported = false)
    override fun delete(entity: T)
    @RestResource(exported = false)
    override fun deleteAll(entities: MutableIterable<T>)
    @RestResource(exported = false)
    override fun deleteAll()
}
