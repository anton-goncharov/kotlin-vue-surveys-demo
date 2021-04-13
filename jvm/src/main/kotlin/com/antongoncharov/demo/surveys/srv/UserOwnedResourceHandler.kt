package com.antongoncharov.demo.surveys.srv

import com.antongoncharov.demo.surveys.logger
import com.antongoncharov.demo.surveys.model.UserOwned
import com.antongoncharov.demo.surveys.security.RequestContext
import org.springframework.data.rest.core.annotation.HandleBeforeCreate
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.stereotype.Component

@Component
@RepositoryEventHandler(UserOwned::class)
class UserOwnedResourceHandler {

    val log by logger()

    @HandleBeforeCreate
    fun handleBeforeSave(userOwned: UserOwned) {
        log.info("Handle before ${userOwned.javaClass} creation")
        userOwned.user = RequestContext.user
    }

}