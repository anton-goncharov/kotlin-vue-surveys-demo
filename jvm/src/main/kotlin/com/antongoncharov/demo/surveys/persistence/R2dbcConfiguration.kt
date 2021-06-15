package com.antongoncharov.demo.surveys.persistence

import io.r2dbc.h2.H2ConnectionConfiguration
import io.r2dbc.h2.H2ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate

@Configuration
class R2dbcConfiguration {

    // TODO load from application.yml
    val connectionFactory =
        H2ConnectionFactory(
            H2ConnectionConfiguration.builder()
            .file("~/surveys")
            .property("DB_CLOSE_DELAY", "-1")
            .property("DB_CLOSE_ON_EXIT", "FALSE")
            .username("sa")
            .build()
        )

    @Bean
    fun r2dbcEntityTemplate() = R2dbcEntityTemplate(connectionFactory)

}