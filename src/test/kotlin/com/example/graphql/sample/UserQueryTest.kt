package com.example.graphql.sample

import java.nio.charset.StandardCharsets.UTF_8
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.util.Base64Utils
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.test.test

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles( "test")
class UserQueryTest(@LocalServerPort port: Int) {

    private val client = WebClient.create("http://localhost:$port")

    @Test
    fun `should handle post`() {
        val authentication = client.get()
            .uri("/login")
            .header("Authorization", "Basic " + Base64Utils
                .encodeToString(("user:user").toByteArray(UTF_8)))
            .exchange()
            .block()!!
            .headers()
            .header("Authorization")

        client.post().uri("/graphql")
            .header("Authorization", authentication[0])
            .syncBody(GraphQLRequest(query = """
                    {
                        findUser(email: "cain.jay@gmail.com"){
                        email
                        createdTime
                    }
                }"""))
            .retrieve().bodyToMono<GraphQLResponse>()
            .test().consumeNextWith { response ->
                val map = response.data as HashMap<*, *>
                val user = map["findUser"] as HashMap<*, *>

                Assertions.assertEquals((user["email"]), "cain.jay@gmail.com")
            }
            .verifyComplete()
    }
}
