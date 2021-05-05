package com.antongoncharov.demo.surveys.security

import com.antongoncharov.demo.surveys.model.User
import org.springframework.stereotype.Component

@Component("requestContext")
class RequestContext {

    companion object CurrentUser {
        // backing thread-local 'static' field
        // it's null when we're using public API (e.g. for registering a new user)
        private val CONTEXT = ThreadLocal<User?>()

        var user: User?
            set(value) { CONTEXT.set(value) }
            get() { return CONTEXT.get() }
    }

}