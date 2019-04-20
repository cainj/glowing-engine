package com.example.graphql.sample

import com.example.graphql.sample.context.MyGraphQLContext
import com.example.graphql.sample.model.User
import com.example.graphql.sample.repository.UserRepository
import graphql.ExecutionInput
import graphql.GraphQL
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.*

@Component
class QueryHandler(private val graphql: GraphQL, val userRepository: UserRepository) {

    private val encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    fun executeQuery(request: GraphQLRequest): Mono<GraphQLResponse> = Mono.subscriberContext()
        .flatMap { ctx ->
            val graphQLContext: MyGraphQLContext = ctx.get("graphQLContext")
            val input = ExecutionInput.newExecutionInput()
                .query(request.query)
                .operationName(request.operationName)
                .variables(request.variables)
                .context(graphQLContext)
                .build()

            Mono.fromFuture(graphql.executeAsync(input))
                .map { executionResult -> executionResult.toGraphQLResponse() }
        }

    fun saveUser(req: ServerRequest): Mono<ServerResponse> = req.bodyToMono(User::class.java).flatMap {user ->
        user.password = encoder.encode(user.password)
        if(user.createdTime == null) user.createdTime = Date()
        ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .syncBody(userRepository.save(user))
    }

}
