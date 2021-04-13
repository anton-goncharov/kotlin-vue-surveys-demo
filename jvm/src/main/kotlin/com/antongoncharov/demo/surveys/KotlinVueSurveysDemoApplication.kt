package com.antongoncharov.demo.surveys

import com.antongoncharov.demo.surveys.model.User
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class KotlinVueSurveysDemoApplication

fun main(args: Array<String>) {
	runApplication<KotlinVueSurveysDemoApplication>(*args)
}
