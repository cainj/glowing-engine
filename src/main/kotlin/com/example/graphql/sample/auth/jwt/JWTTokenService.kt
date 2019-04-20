package com.example.graphql.sample.auth.jwt

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import java.time.Period
import java.util.*
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component

@Component
class JWTTokenService {

    fun generateToken(name: String?, credentials: Any?, authorities: Collection<GrantedAuthority>): String {
        val claimSet = JWTClaimsSet.Builder()
            .subject(name)
            .issuer("jay.io")
            .expirationTime(Date(createExpiration()))
            .claim("roles", authorities.joinToString(",") { it.authority })
            .build()

        val signedJWT = SignedJWT(JWSHeader(JWSAlgorithm.HS256), claimSet)

        try {
            signedJWT.sign(MACSigner(JWTSecrets.DEFAULT_SECRET))
        } catch (e: JOSEException) {
            logger.error("")
        }

        return signedJWT.serialize()
    }

    private fun createExpiration(): Long {
        return Date().toInstant()
            .plus(Period.ofDays(1))
            .toEpochMilli()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(JWTTokenService::class.java.name)
    }
}
