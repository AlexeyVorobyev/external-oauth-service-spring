package org.lexxv.externaloauthservicespring.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.lexxv.externaloauthservicespring.security.filters.JwtAuthorizationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
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
@EnableMethodSecurity(
    securedEnabled = true,
    prePostEnabled = true
)
class SecurityConfig {

    @Autowired
    private final lateinit var jwtService: JwtService

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
        /**
         * Конфигурация cors
         * */
        http.cors { cors ->
            cors.configurationSource(corsConfigurationSource())
        }

        /**
         * Отключение стандартной формы логина
         * */
        http.csrf { csrf ->
            csrf.disable().formLogin { formLogin ->
                formLogin.disable().logout { logout ->
                    logout.disable()
                }
            }
        }

        /**
         * Ошибка возвращаемая после некорректной аутентификации
         * */
        http.exceptionHandling {
            it.authenticationEntryPoint { _: HttpServletRequest, response: HttpServletResponse, _: AuthenticationException ->
                response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.reasonPhrase)
            }
        }

        /**
         * Отключение сессий
         * */
        http.sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        http.authorizeHttpRequests {
            it.requestMatchers(
                AntPathRequestMatcher("/graphql*")
            )
                .authenticated()
                .anyRequest()
                .permitAll()
        }
            .addFilterBefore(
                JwtAuthorizationFilter(
                    jwtService,
                    listOf(
                        AntPathRequestMatcher("/graphql*")
                    )
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

