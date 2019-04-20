package com.example.graphql.sample.config

import com.example.graphql.sample.auth.MysqlUserDetailService
import com.example.graphql.sample.auth.basic.BasicAuthenticationSuccessHandler
import com.example.graphql.sample.auth.bearer.BearerTokenReactiveAuthenticationManager
import com.example.graphql.sample.auth.bearer.ServerHttpBearerAuthenticationConverter
import com.example.graphql.sample.auth.jwt.JWTTokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers

@ConfigurationProperties(prefix = "auth")
@Configuration
class AuthConfig {

    @Autowired
    private var jwtTokenService: JWTTokenService? = null

    @Autowired
    private var mysqlUserDetailsService: MysqlUserDetailService? = null

    lateinit var requiredAuthPaths: Array<String>

    lateinit var accessiblePaths: Array<String>

    @Suppress("SpreadOperator")
    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http
            .authorizeExchange()
            .pathMatchers(* accessiblePaths)
            .permitAll()
            .and()
            .addFilterAt(basicAuthenticationFilter(), SecurityWebFiltersOrder.HTTP_BASIC)
            .authorizeExchange()
            .pathMatchers(* requiredAuthPaths)
            .authenticated()
            .and()
            .csrf().disable()
            .addFilterAt(bearerAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)

        return http.build()
    }

    /**
     * Use the already implemented logic in  AuthenticationWebFilter and set a custom
     * SuccessHandler that will return a JWT when a user is authenticated with user/password
     * Create an AuthenticationManager using the UserDetailsService defined above
     *
     * @return [AuthenticationWebFilter]
     */
    private fun basicAuthenticationFilter(): AuthenticationWebFilter {
        val userDetailService =
            UserDetailsRepositoryReactiveAuthenticationManager(mysqlUserDetailsService).also {
                it.setUserDetailsPasswordService(mysqlUserDetailsService)
            }
        return AuthenticationWebFilter(userDetailService).also {
            it.setAuthenticationSuccessHandler(BasicAuthenticationSuccessHandler(jwtTokenService!!))
        }
    }

    /**
     * Use the already implemented logic by AuthenticationWebFilter and set a custom
     * converter that will handle requests containing a Bearer token inside
     * the HTTP Authorization header.
     * Set a dummy authentication manager to this filter, it's not needed because
     * the converter handles this.
     *
     * @return [AuthenticationWebFilter] that will authorize requests containing a JWT
     */
    @Suppress("SpreadOperator")
    private fun bearerAuthenticationFilter(): AuthenticationWebFilter =
        AuthenticationWebFilter(BearerTokenReactiveAuthenticationManager()).also { webFilter ->
            webFilter.setServerAuthenticationConverter(ServerHttpBearerAuthenticationConverter())
            webFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(* requiredAuthPaths))
        }
}
