package com.antongoncharov.demo.surveys.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import javax.servlet.http.HttpServletResponse

@Configuration
class SecuritySupport {

    val mapper = jacksonObjectMapper()

    @Bean
    protected fun corsConfigurationSource(): CorsConfigurationSource? { // TODO has option from .reactive package
        val source = UrlBasedCorsConfigurationSource() // TODO has option from .reactive package
        val config = CorsConfiguration()
//        config.allowCredentials = true
        config.addAllowedOrigin("*")
        config.addAllowedHeader("*")
        config.addExposedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        return source
    }

    @Bean
    protected fun authenticationEntryPoint(): AuthenticationEntryPoint {
        return AuthenticationEntryPoint { request, response, authException ->
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            val body: Map<String, Any> = mapOf(
                "code" to HttpServletResponse.SC_UNAUTHORIZED,
                "payload" to "You need to login first in order to perform this action."
            )
            mapper.writeValue(response.getOutputStream(), body)
        }
    }

    @Bean
    fun accessDeniedHandler(): AccessDeniedHandler {
        return AccessDeniedHandler { request, response, accessDeniedException ->
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.status = HttpServletResponse.SC_FORBIDDEN
            val body: Map<String, Any> = mapOf(
                "code" to HttpServletResponse.SC_FORBIDDEN,
                "payload" to "You don't have required role to perform this action."
            )
            mapper.writeValue(response.outputStream, body)
        }
    }

}