package com.example.graphql.sample.directives

import com.expedia.graphql.annotations.GraphQLDirective
import graphql.introspection.Introspection.DirectiveLocation.*

/**
 * Valid on all locations
 */
@GraphQLDirective(
    locations = [
        QUERY,
        MUTATION,
        FIELD,
        FRAGMENT_DEFINITION,
        FRAGMENT_SPREAD,
        INLINE_FRAGMENT,
        SCHEMA,
        SCALAR,
        OBJECT,
        FIELD_DEFINITION,
        ARGUMENT_DEFINITION,
        INTERFACE,
        UNION,
        ENUM,
        ENUM_VALUE,
        INPUT_OBJECT,
        INPUT_FIELD_DEFINITION
    ]
)
annotation class SimpleDirective
