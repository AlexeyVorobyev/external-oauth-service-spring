package org.lexxv.externaloauthservicespring.security

import org.lexxv.externaloauthservicespring.security.filter.JwtFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


/**
 * Конфигурация безопасности доступа.
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig {

    @Autowired
    private lateinit var authenticationService: AuthenticationService

    /**
     * Бин конфигурирования доступа.
     *
     * @param http Объект конфигурации доступов
     * @param authenticationConverter Объект преобразований JWT токена
     *
     * @return Объект цепочки обработки запроса
     */
    @Bean
    fun filterChain(
        http: HttpSecurity,
    ): SecurityFilterChain {
        http.cors { cors ->
            cors.configurationSource(corsConfigurationSource())
        }

        http.csrf { obj: AbstractHttpConfigurer<*, *> -> obj.disable() }
            .authorizeHttpRequests { authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry.requestMatchers(
                    "/graphql/**"
                ).authenticated()
            }
            .httpBasic(Customizer.withDefaults())
            .sessionManagement { httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }
            .securityMatcher("/graphql/some")
            .addFilterBefore(
                JwtFilter(
                    authenticationService
                ),
                UsernamePasswordAuthenticationFilter::class.java
            )

        return http.build()
    }

    /**
     * Конфигурация политик CORS.
     */
    private fun corsConfigurationSource(): CorsConfigurationSource {
        val allowedAll = listOf("*")
        val configuration = CorsConfiguration().apply {
            allowedOrigins = allowedAll
            allowedMethods = allowedAll
            allowedHeaders = allowedAll
            exposedHeaders = allowedAll
        }
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}

