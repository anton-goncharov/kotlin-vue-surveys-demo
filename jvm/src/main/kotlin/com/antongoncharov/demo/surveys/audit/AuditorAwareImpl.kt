package com.antongoncharov.demo.surveys.audit

import com.antongoncharov.demo.surveys.model.User
import com.antongoncharov.demo.surveys.security.RequestContext
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuditorAwareImpl: AuditorAware<User> {

    override fun getCurrentAuditor(): Optional<User> {
        return Optional.ofNullable(RequestContext.user)
    }

}