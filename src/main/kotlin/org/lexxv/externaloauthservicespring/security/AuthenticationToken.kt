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
        isAuthenticated = true
    }

    /**
     * Ключи основных данных токена
     */
    companion object Claims {
        const val ID = "id"
        const val EMAIL = "email"
        const val ROLE = "role"
    }

    override fun getCredentials(): MutableMap<String, Any> {
        println(jwtService.getClaims(token))
        return jwtService.getClaims(token)
    }


    override fun getPrincipal(): Any {
        TODO("Not yet implemented")
    }

}