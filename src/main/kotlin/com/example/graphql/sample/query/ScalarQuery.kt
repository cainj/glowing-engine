package com.example.graphql.sample.query

import com.example.graphql.sample.mutation.Mutation
import com.expedia.graphql.annotations.GraphQLDescription
import com.expedia.graphql.annotations.GraphQLID
import java.util.*
import org.springframework.stereotype.Component

/**
 * Simple query that exposes custom scalar.
 */
@Component
class ScalarQuery : Query {

    @GraphQLDescription("generates random UUID")
    fun generateRandomUUID() = UUID.randomUUID()

    fun findPersonById(id: Int) = Person(id, "Nelson")
}

@Component
class ScalarMutation : Mutation {
    fun addPerson(person: Person): Person = person
}

data class Person(
    @GraphQLID
    val id: Int,

    val name: String
)
