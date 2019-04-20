package com.example.graphql.sample.auth.jwt

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JWSVerifier
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.SignedJWT
import java.text.ParseException
import java.time.Instant
import java.util.*
import reactor.core.publisher.Mono

internal class JWTCustomVerifier {

    private val jwsVerifier: JWSVerifier? = this.buildJWSVerifier()

    private val isNotExpired = { token: SignedJWT -> getExpirationDate(token)!!.after(Date.from(Instant.now())) }

    private val validSignature = { token: SignedJWT ->
        try {
            token.verify(this.jwsVerifier)
        } catch (e: JOSEException) {
            e.printStackTrace()
            false
        }
    }

    fun check(token: String): Mono<SignedJWT> {
        return Mono.justOrEmpty(createJWS(token))
            .filter(isNotExpired)
            .filter(validSignature)
    }

    private fun buildJWSVerifier(): MACVerifier? =
        try {
            MACVerifier(JWTSecrets.DEFAULT_SECRET)
        } catch (e: JOSEException) {
            e.printStackTrace()
            null
        }

    private fun createJWS(token: String): SignedJWT? =
        try {
            SignedJWT.parse(token)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }

    private fun getExpirationDate(token: SignedJWT): Date? =
        try {
            token.jwtClaimsSet
                .expirationTime
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
}
