package com.antongoncharov.demo.surveys.api

import com.antongoncharov.demo.surveys.logger
import com.antongoncharov.demo.surveys.model.UserPreferences
import com.antongoncharov.demo.surveys.model.dto.UserPreferencesRequest
import com.antongoncharov.demo.surveys.persistence.TagRepository
import com.antongoncharov.demo.surveys.persistence.UserPreferencesRepository
import com.antongoncharov.demo.surveys.security.RequestContext
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserPreferencesController(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val tagRepository: TagRepository
) {

    val log by logger()

    @GetMapping("/preferences")
    fun getPreferences(): ResponseEntity<Any> {
        log.info("GET /preferences")
        val findForCurrentUser = userPreferencesRepository.findForCurrentUser()
        return findForCurrentUser?.let { ResponseEntity.ok().body(it) } ?:
            ResponseEntity.notFound().build()
    }

    @PostMapping("/preferences")
    fun savePreferences(@RequestBody preferencesRequest: UserPreferencesRequest): ResponseEntity<Any> {
        log.info("POST /preferences > $preferencesRequest")

        val currentPreferences = userPreferencesRepository.findForCurrentUser() ?: UserPreferences()

        currentPreferences.user = RequestContext.user
        currentPreferences.displayUntagged = preferencesRequest.displayUntagged
        val currentLayout = currentPreferences.mainPageLayout
        if (currentLayout.isEmpty()) {
            // a straightforward case, when current preferences are empty
            val tags = tagRepository.findByShortNameIn(preferencesRequest.mainPageLayout)
            for (tagName in preferencesRequest.mainPageLayout) {
                currentLayout.add(tags.find { it.shortName == tagName }!!)
            }
        } else {
            // delete those not in new preferences
            currentLayout.filter { !preferencesRequest.mainPageLayout.contains(it.shortName) }
                         .forEach { currentLayout.remove(it) }
            // add or reorder those in the preferences
            preferencesRequest.mainPageLayout.withIndex().forEach { (index, tagName) ->
                // find among existing tag preferences or fetch from DB
                val tag = currentLayout.find { t -> t.shortName == tagName }
                                      ?.also { currentLayout.remove(it) }
                                      ?: tagRepository.findByShortName(tagName)
                // put into right position
                tag?.let { currentLayout.add(index, tag) } ?:
                    return ResponseEntity.badRequest().build()
            }
        }
        userPreferencesRepository.save(currentPreferences)
        return ResponseEntity.ok().build()
    }

}