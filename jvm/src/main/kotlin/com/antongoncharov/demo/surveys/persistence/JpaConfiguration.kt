package com.antongoncharov.demo.surveys.persistence

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.hibernate5.LocalSessionFactoryBean

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = ["com.antongoncharov.demo.surveys.persistence"])
class JpaConfiguration {

}