package com.example.graphql.sample.directives

import graphql.schema.DataFetcher
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.idl.SchemaDirectiveWiring
import graphql.schema.idl.SchemaDirectiveWiringEnvironment

class LowercaseDirectiveWiring : SchemaDirectiveWiring {

    override fun onField(env: SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition>): GraphQLFieldDefinition {
        val field = env.element
        val originalDataFetcher: DataFetcher<Any> = field.dataFetcher

        val lowerCaseFetcher = DataFetcher<String> { dataEnv ->
            originalDataFetcher.get(dataEnv).toString().toLowerCase()
        }
        return field.transform { it.dataFetcher(lowerCaseFetcher) }
    }
}
