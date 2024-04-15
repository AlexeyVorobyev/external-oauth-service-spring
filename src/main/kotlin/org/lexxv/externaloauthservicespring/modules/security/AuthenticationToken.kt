package org.lexxv.externaloauthservicespring.modules.security

import io.jsonwebtoken.Claims
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

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