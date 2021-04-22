package com.antongoncharov.demo.surveys.security

import com.antongoncharov.demo.surveys.persistence.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor
import org.springframework.web.cors.CorsConfigurationSource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

import java.util.HashMap




@EnableWebSecurity
@Configuration
class SecurityConfig(
    private val userRepository: UserRepository,
    private val jwtTokenFilter: JwtTokenFilter,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
    private val accessDeniedHandler: AccessDeniedHandler
): WebSecurityConfigurerAdapter() {


    override fun configure(httpSecurity: HttpSecurity) {

        // Enable CORS and disable CSRF
        httpSecurity
            .cors()
            .and()
            .csrf().disable()

        // Set session management to stateless
        httpSecurity
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and();

        // Set permissions on endpoints
        httpSecurity
            .authorizeRequests()
                .antMatchers("/api/v1/").permitAll() // hal browser
                .antMatchers("/api/v1/public/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
            .anyRequest().authenticated()

        // Add JWT token filter
        httpSecurity.addFilterBefore(
            jwtTokenFilter,
            UsernamePasswordAuthenticationFilter::class.java
        )
            .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
            .and()
            .exceptionHandling().accessDeniedHandler(accessDeniedHandler)

        httpSecurity.headers().frameOptions().disable()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        // configure authentication manager
        auth.userDetailsService(object: UserDetailsService {
            override fun loadUserByUsername(username: String?): UserDetails {
                if (username == null || username == "") {
                    throw UsernameNotFoundException(username)
                }
                val user = userRepository.findByEmail(username)
                    ?: throw UsernameNotFoundException(username)
                return user.asUserDetails()
            }
        })
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }

}