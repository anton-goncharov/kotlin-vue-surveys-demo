package com.antongoncharov.demo.surveys.api

import com.antongoncharov.demo.surveys.logger
import com.antongoncharov.demo.surveys.model.User
import com.antongoncharov.demo.surveys.model.dto.LoginRequest
import com.antongoncharov.demo.surveys.model.dto.RegisterRequest
import com.antongoncharov.demo.surveys.persistence.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PostMapping
import com.antongoncharov.demo.surveys.security.JwtTokenUtil
import org.slf4j.Logger
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["public"])
class AuthController (
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenUtil: JwtTokenUtil
) {

    val LOG by logger()

    @PostMapping("register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<Any>? {
        LOG.info("Registering new user: ${request.email}")
        if (userRepository.findByEmail(request.email) != null) {
            LOG.info("User ${request.email} already exists")
            return ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
        val user = User(
            name = request.name,
            email = request.email,
            password = passwordEncoder.encode(request.password)
        )
        val saved = userRepository.save(user)
        return ResponseEntity.ok().body(saved)
    }

    @PostMapping("login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Any>? {
        LOG.info("Signing in: ${request.email}")
        return try {
            val authenticate: Authentication = authenticationManager
                .authenticate(UsernamePasswordAuthenticationToken(request.email, request.password))
            val principal = authenticate.getPrincipal()
            val user = userRepository.findByEmail(request.email);
            ResponseEntity.ok()
                .header(
                    HttpHeaders.AUTHORIZATION,
                    jwtTokenUtil.generateToken(principal as UserDetails)
                )
                .body(user)
        } catch (ex: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

}