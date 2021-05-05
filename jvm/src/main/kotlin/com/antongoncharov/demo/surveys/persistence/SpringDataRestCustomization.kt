package com.antongoncharov.demo.surveys.persistence

import com.antongoncharov.demo.surveys.logger
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.CorsRegistry

@Component
class SpringDataRestCustomization: RepositoryRestConfigurer {

    val log by logger()

//    override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration?, cors: CorsRegistry?) {
//        if (cors != null) {
//            cors.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .allowCredentials(true)
//        }
//    }
}