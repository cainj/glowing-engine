package com.example.graphql.sample.auth.bearer

import com.example.graphql.sample.auth.jwt.AuthorizationHeaderPayload
import com.example.graphql.sample.auth.jwt.JWTCustomVerifier
import com.example.graphql.sample.auth.jwt.UsernamePasswordAuthenticationBearer
import org.springframework.security.core.Authentication
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class ServerHttpBearerAuthenticationConverter : (ServerWebExchange) -> Mono<Authentication> {

    companion object {

        private const val BEARER = "Bearer "
        private val matchBearerLength = { authValue: String -> authValue.length > BEARER.length }
        private val isolateBearerValue = { authValue: String ->
            Mono.justOrEmpty(authValue.substring(BEARER.length))
        }

        private val jwtVerifier = JWTCustomVerifier()
    }

    override fun invoke(serverWebExchange: ServerWebExchange): Mono<Authentication> {
        return Mono.justOrEmpty(serverWebExchange)
            .flatMap(AuthorizationHeaderPayload::extract)
            .filter(matchBearerLength)
            .flatMap(isolateBearerValue)
            .flatMap(jwtVerifier::check)
            .flatMap(UsernamePasswordAuthenticationBearer::create).log()
    }
}
