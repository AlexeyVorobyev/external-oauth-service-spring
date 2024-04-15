package org.lexxv.externaloauthservicespring.modules.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.lexxv.externaloauthservicespring.modules.security.AuthenticationService
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean

class JwtFilter(
    private val authenticationService: AuthenticationService
) : GenericFilterBean() {

    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val authentication: Authentication = authenticationService.getAuthentication(request as HttpServletRequest)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (exp: Exception) {
            val httpResponse = response as HttpServletResponse
            httpResponse.status = HttpServletResponse.SC_UNAUTHORIZED

            val writer = httpResponse.writer
            writer.print(exp.message)
            writer.flush()
            writer.close()
        }

        filterChain.doFilter(request, response)
    }
}