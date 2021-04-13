package com.antongoncharov.demo.surveys.config

import com.antongoncharov.demo.surveys.model.JpaPersistable
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer

import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.stereotype.Component

@Component
class ExposeEntityIdRestConfiguration: RepositoryRestConfigurer {

    override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration) {
        config.exposeIdsFor(JpaPersistable::class.java)
    }

}