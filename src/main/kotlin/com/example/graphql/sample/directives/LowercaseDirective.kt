package com.example.graphql.sample.directives

import com.expedia.graphql.annotations.GraphQLDirective

@GraphQLDirective(name = "lowercase", description = "Modifies the string field to lowercase")
annotation class LowercaseDirective
