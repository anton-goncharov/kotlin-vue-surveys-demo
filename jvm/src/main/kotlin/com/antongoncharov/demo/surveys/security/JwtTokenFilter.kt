package com.antongoncharov.demo.surveys.security

import com.antongoncharov.demo.surveys.persistence.UserRepository
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails

/**
 * Extracts token from authentication header, decrypts JWT token
 * and populates SecurityContextHolder container with user data.
 */
@Component
class JwtTokenFilter(
    private val jwtTokenUtil: JwtTokenUtil,
    private val userRepository: UserRepository
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Get authorization header
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        // Get jwt token and validate
        val token = header.split(" ").toTypedArray()[1].trim { it <= ' ' }

        // Get user identity and set it on the spring security context
        val user = userRepository.findByEmail(jwtTokenUtil.getUsernameFromToken(token))
            ?: return filterChain.doFilter(request, response)

        // Set current user to request context
        // TODO extract to a separate filter
        RequestContext.user = user

        val userDetails: UserDetails = user.asUserDetails()
        if (!jwtTokenUtil.validateToken(token, userDetails)) {
            return filterChain.doFilter(request, response)
        }

        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

        SecurityContextHolder.getContext().authentication = authentication
        filterChain.doFilter(request, response)
    }

}