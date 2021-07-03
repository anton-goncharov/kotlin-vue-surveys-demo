package com.antongoncharov.demo.surveys.persistence

import io.r2dbc.h2.H2ConnectionConfiguration
import io.r2dbc.h2.H2ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate

@Configuration
class R2dbcConfiguration {

    @Value("spring.datasource.url")
    lateinit var dataSourceUrl: String

    @Value("spring.datasource.username")
    lateinit var dataSourceUsername: String

    @Value("spring.datasource.password")
    lateinit var dataSourcePassword: String

    @Bean
    fun r2dbcEntityTemplate(): R2dbcEntityTemplate {
        val connectionFactory =
            H2ConnectionFactory(
                H2ConnectionConfiguration.builder()
                    .url(dataSourceUrl)
//            .file("~/surveys")
//            .property("DB_CLOSE_DELAY", "-1")
//            .property("DB_CLOSE_ON_EXIT", "FALSE")
                    .username(dataSourceUsername)
                    .password(dataSourcePassword)
                    .build()
            )
         return R2dbcEntityTemplate(connectionFactory)
    }

}