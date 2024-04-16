package org.lexxv.externaloauthservicespring.security

import io.jsonwebtoken.Claims
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

/**
 * Класс, описывающий аутентификационный аксесс токен
 *
 * @author alexey vorobyev <mister.alex49@yandex.ru>
 * */
class AuthenticationToken(
    private val token: String,
    private val jwtService: JwtService,
    authorities: Collection<GrantedAuthority?>?
) : AbstractAuthenticationToken(authorities) {
    init {
        jwtService.validateAccessToken(token)
        isAuthenticated = true
    }

    override fun getCredentials(): Claims {
        return jwtService.getAccessClaims(token)
    }

    override fun getPrincipal(): Any {
        return token
    }
}