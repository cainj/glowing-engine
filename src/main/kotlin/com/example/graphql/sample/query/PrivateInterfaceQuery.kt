package com.example.graphql.sample.query

import com.example.graphql.sample.model.HidesInheritance
import com.expedia.graphql.annotations.GraphQLDescription
import org.springframework.stereotype.Component

@Component
class PrivateInterfaceQuery : Query {

    @GraphQLDescription("this query returns class implementing private interface which is not exposed in the schema")
    fun queryForObjectWithPrivateInterface(): HidesInheritance = HidesInheritance(id = 123)
}
