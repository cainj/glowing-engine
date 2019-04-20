package com.example.graphql.sample.query

import com.example.graphql.sample.context.MyGraphQLContext
import com.example.graphql.sample.model.ContextualResponse
import com.expedia.graphql.annotations.GraphQLContext
import com.expedia.graphql.annotations.GraphQLDescription
import org.springframework.stereotype.Component

/**
 * Example usage of [GraphQLContext] annotation. By using this annotation context parameter won't be exposed as in the
 * schema and will be automatically autowired at runtime using value from the environment.
 *
 * @see [com.example.graphql.sample.context.MyGraphQLContextWebFilter]
 * @see [com.example.graphql.execution.FunctionDataFetcher]
 */
@Component
class ContextualQuery : Query {

    @GraphQLDescription("query that uses GraphQLContext context")
    fun contextualQuery(
        @GraphQLDescription("some value that will be returned to the user")
        value: Int,
        @GraphQLContext context: MyGraphQLContext
    ): ContextualResponse = ContextualResponse(value, context.myCustomValue)
}
