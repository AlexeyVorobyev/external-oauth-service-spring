package org.lexxv.externaloauthservicespring.modules.security

import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    @Autowired
    private val jwtService: JwtService
) {
    private val AUTH_TOKEN_HEADER_NAME = "Authorization"

    fun getAuthentication(request: HttpServletRequest): Authentication {
        val authToken = request
            .getHeader(AUTH_TOKEN_HEADER_NAME)
            .split(' ')[1]

        if (authToken == null) {
            throw BadCredentialsException("Invalid API Key")
        }

        return AuthenticationToken(authToken, jwtService, AuthorityUtils.NO_AUTHORITIES)
    }
}