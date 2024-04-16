package org.lexxv.externaloauthservicespring.security

import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.stereotype.Service

/**
 * Сервис аутентификации по токену и преобразование полученного токена в токен системы
 *
 * @author alexey vorobyev <mister.alex49@yandex.ru>
 * */
@Service
class AuthenticationService(
    @Autowired
    private val jwtService: JwtService
) {
    private val AUTH_TOKEN_HEADER_NAME = "Authorization"

    fun getAuthenticationToken(request: HttpServletRequest): Authentication {

        val authToken: String

        val headerValue = request.getHeader(AUTH_TOKEN_HEADER_NAME)

        if (headerValue == null) {
            throw BadCredentialsException("Authorization header is missing")
        }

        headerValue.split(' ')
            .let { arr ->
                if (arr.size == 1) {
                    throw BadCredentialsException("Invalid auth token")
                }
                else if (arr.size == 2) {
                    authToken = arr[1]
                }
                else {
                    throw BadCredentialsException("Invalid auth token")
                }
            }

        return AuthenticationToken(
            authToken,
            jwtService,
            AuthorityUtils.NO_AUTHORITIES
        )
    }
}