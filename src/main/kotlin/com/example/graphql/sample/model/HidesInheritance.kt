package com.example.graphql.sample.model

import com.expedia.graphql.annotations.GraphQLDescription

@GraphQLDescription("Class implementing private interface that is not exposed in the schema")
data class HidesInheritance(val id: Int) : PrivateInterface {

    override val value: String
        get() = "Implementation of a method from a private interface"
}

private interface PrivateInterface {
    val value: String
}
