package com.antongoncharov.demo.surveys

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcDataAutoConfiguration
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [R2dbcRepositoriesAutoConfiguration::class, R2dbcDataAutoConfiguration::class, R2dbcAutoConfiguration::class]
)
class KotlinVueSurveysDemoApplication

fun main(args: Array<String>) {
	runApplication<KotlinVueSurveysDemoApplication>(*args)
}
