package org.lexxv.externaloauthservicespring.security.filters

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.lexxv.externaloauthservicespring.security.AuthenticationToken
import org.lexxv.externaloauthservicespring.security.JwtService
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtAuthorizationFilter(
    private val jwtService: JwtService,
    private val invokedMatchers: List<AntPathRequestMatcher>
) : OncePerRequestFilter() {
    private val AUTH_TOKEN_HEADER_NAME = "Authorization"

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        println("JwtAuthorizationFilter invoked")

        val headerValue = request.getHeader(AUTH_TOKEN_HEADER_NAME)

        try {
            if (headerValue == null) {
                throw BadCredentialsException("Authorization header is missing")
            }

            val authToken: String

            headerValue.split(' ')
                .let { arr ->
                    if (arr.size == 1) {
                        throw BadCredentialsException("Invalid auth token")
                    } else if (arr.size == 2) {
                        authToken = arr[1]
                    } else {
                        throw BadCredentialsException("Invalid auth token")
                    }
                }

            jwtService.validateAccessToken(authToken)

            SecurityContextHolder.getContext().authentication = AuthenticationToken(
                authToken,
                jwtService,
                AuthorityUtils.NO_AUTHORITIES
            )
        } catch (exp: Exception) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.setHeader("error-message", exp.message)
        }

        filterChain.doFilter(request, response)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return !invokedMatchers
            .stream()
            .anyMatch {
                it.matches(request)
            }
    }
}