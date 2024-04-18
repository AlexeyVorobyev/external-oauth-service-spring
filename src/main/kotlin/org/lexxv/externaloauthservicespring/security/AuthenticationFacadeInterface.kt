package org.lexxv.externaloauthservicespring.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

interface AuthenticationFacadeInterface {
    fun getAuthentication(): Authentication {
        return SecurityContextHolder.getContext().authentication
    }
}
