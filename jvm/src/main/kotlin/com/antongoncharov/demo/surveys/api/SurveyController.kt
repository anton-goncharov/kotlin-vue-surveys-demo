package com.antongoncharov.demo.surveys.api

import com.antongoncharov.demo.surveys.logger
import com.antongoncharov.demo.surveys.model.Survey
import com.antongoncharov.demo.surveys.model.Tag
import com.antongoncharov.demo.surveys.model.spec.SurveySpec
import com.antongoncharov.demo.surveys.persistence.SurveyRepository
import com.antongoncharov.demo.surveys.persistence.TagRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class SurveyController(
    private val tagRepository: TagRepository,
    private val surveyRepository: SurveyRepository
) {

    val log by logger()

    @GetMapping("surveys/search")
    @ResponseBody
    fun search(@RequestParam searchParams: Map<String,String>,
               pagedAssembler: PagedResourcesAssembler<Survey>,
               pageable: Pageable): ResponseEntity<Any> {
        log.info("GET /surveys/search, params: $searchParams")
        val surveySpec = SurveySpec(searchParams)
        val result = surveyRepository.findAll(surveySpec, pageable)
        return ResponseEntity.ok(pagedAssembler.toModel(result))
    }

    /**
     * Update survey tags (rewrites the list)
     */
    @PostMapping("surveys/{surveyUuid}/tags", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateSurveyTags(@RequestBody tagUuids: MutableList<String>,
                         @PathVariable("surveyUuid") surveyUuid: String): ResponseEntity<Any> {
        log.info("GET /surveys/${surveyUuid}/tags")
        val tags = tagRepository.findAllById(tagUuids.map { UUID.fromString(it) })
        val surveyOpt = surveyRepository.findById(UUID.fromString(surveyUuid))
        if (!surveyOpt.isPresent) {
            return ResponseEntity.notFound().build()
        }
        surveyOpt.ifPresent {
            it.tags.clear()
            it.tags.addAll(tags)
            surveyRepository.save(it)
        }
        return ResponseEntity.noContent().build()
    }

}