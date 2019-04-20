package com.example.graphql.sample.auth.jwt

import com.nimbusds.jwt.SignedJWT
import java.text.ParseException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import reactor.core.publisher.Mono

/**
 * This converter takes a SignedJWT and extracts all information
 * contained to build an Authentication Object
 * The signed JWT has already been verified.
 *
 */
object UsernamePasswordAuthenticationBearer {

    @Suppress("SpreadOperator")
    fun create(signedJWTMono: SignedJWT): Mono<Authentication> = try {
        val subject = signedJWTMono.jwtClaimsSet.subject
        val auths = signedJWTMono.jwtClaimsSet.getClaim("roles") as String
        val authorities = listOf(*auths.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            .map { a -> SimpleGrantedAuthority(a) }
        Mono.justOrEmpty(UsernamePasswordAuthenticationToken(subject, null, authorities))
    } catch (e: ParseException) {
        Mono.empty()
    }
}
