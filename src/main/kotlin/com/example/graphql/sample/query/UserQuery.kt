package com.example.graphql.sample.query

import com.example.graphql.sample.model.User
import com.example.graphql.sample.repository.UserRepository
import com.expedia.graphql.annotations.GraphQLDescription
import java.util.concurrent.CompletableFuture
import org.springframework.stereotype.Component

@Component
class UserQuery(private val userRepository: UserRepository) : Query {

    @GraphQLDescription("gets a user")
    fun findUser(
        @GraphQLDescription("The email")
        email: String
    ): CompletableFuture<User?> {
        val future = CompletableFuture<User?>()
        future.complete(userRepository.findById(email).orElse(null))
        return future
    }
}
