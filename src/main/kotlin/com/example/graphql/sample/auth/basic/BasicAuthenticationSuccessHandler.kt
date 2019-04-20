package com.example.graphql.sample.auth.basic

import com.example.graphql.sample.auth.jwt.JWTTokenService
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

/**
 * On success authentication a signed JWT object is serialized and added
 * in the authorization header as a bearer token
 */
@Component
class BasicAuthenticationSuccessHandler(val jwtTokenService: JWTTokenService) : ServerAuthenticationSuccessHandler {

    /**
     * A successful authentication object us used to create a JWT object and
     * added in the authorization header of the current WebExchange
     *
     * @param webFilterExchange
     * @param authentication
     * @return
     */
    @Override
    override fun onAuthenticationSuccess(
        webFilterExchange: WebFilterExchange?,
        authentication: Authentication?
    ): Mono<Void> {
        // Create and attach a JWT before passing the exchange to the filter chain
        val exchange = webFilterExchange!!.exchange
        exchange.response.headers.add(HttpHeaders.AUTHORIZATION, getHttpAuthHeaderValue(authentication!!))
        return webFilterExchange.chain.filter(exchange)
    }

    private fun getHttpAuthHeaderValue(authentication: Authentication): String =
        arrayOf("Bearer", tokenFromAuthentication(authentication)).joinToString(" ")

    private fun tokenFromAuthentication(authentication: Authentication): String =
        jwtTokenService.generateToken(authentication.name, authentication.credentials, authentication.authorities)
}
