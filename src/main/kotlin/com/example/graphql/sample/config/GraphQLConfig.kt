package com.example.graphql.sample.config

import com.example.graphql.sample.datafetchers.CustomDataFetcherFactoryProvider
import com.example.graphql.sample.datafetchers.SpringDataFetcherFactory
import com.example.graphql.sample.directives.DirectiveWiringFactory
import com.example.graphql.sample.directives.LowercaseDirectiveWiring
import com.example.graphql.sample.exceptions.CustomDataFetcherExceptionHandler
import com.example.graphql.sample.extension.CustomSchemaGeneratorHooks
import com.example.graphql.sample.mutation.Mutation
import com.example.graphql.sample.query.Query
import com.expedia.graphql.DirectiveWiringHelper
import com.expedia.graphql.SchemaGeneratorConfig
import com.expedia.graphql.TopLevelObject
import com.expedia.graphql.execution.KotlinDataFetcherFactoryProvider
import com.expedia.graphql.hooks.SchemaGeneratorHooks
import com.expedia.graphql.toSchema
import graphql.GraphQL
import graphql.execution.AsyncExecutionStrategy
import graphql.execution.AsyncSerialExecutionStrategy
import graphql.execution.DataFetcherExceptionHandler
import graphql.schema.GraphQLSchema
import graphql.schema.idl.SchemaPrinter
import javax.validation.Validator
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GraphQLConfig {

    private val logger =
        LoggerFactory.getLogger(GraphQLConfig::class.java.name)

    @Bean
    fun wiringFactory() = DirectiveWiringFactory()

    @Bean
    fun hooks(validator: Validator, wiringFactory: DirectiveWiringFactory) =
        CustomSchemaGeneratorHooks(
            validator,
            DirectiveWiringHelper(
                wiringFactory,
                mapOf("lowercase" to LowercaseDirectiveWiring())
            )
        )

    @Bean
    fun dataFetcherFactoryProvider(springDataFetcherFactory: SpringDataFetcherFactory, hooks: SchemaGeneratorHooks) =
        CustomDataFetcherFactoryProvider(springDataFetcherFactory, hooks)

    @Bean
    fun schemaConfig(
        hooks: SchemaGeneratorHooks,
        dataFetcherFactoryProvider: KotlinDataFetcherFactoryProvider
    ): SchemaGeneratorConfig =
        SchemaGeneratorConfig(
            supportedPackages = listOf("com.example"),
            hooks = hooks,
            dataFetcherFactoryProvider = dataFetcherFactoryProvider
        )

    @Bean
    fun schemaPrinter() = SchemaPrinter(
        SchemaPrinter.Options.defaultOptions()
            .includeScalarTypes(true)
            .includeExtendedScalarTypes(true)
            .includeIntrospectionTypes(true)
            .includeSchemaDefintion(true)
    )

    @Bean
    fun schema(
        queries: List<Query>,
        mutations: List<Mutation>,
        schemaConfig: SchemaGeneratorConfig,
        schemaPrinter: SchemaPrinter
    ): GraphQLSchema {
        fun List<Any>.toTopLevelObjectDefs() = this.map {
            TopLevelObject(it)
        }

        val schema = toSchema(
            config = schemaConfig,
            queries = queries.toTopLevelObjectDefs(),
            mutations = mutations.toTopLevelObjectDefs()
        )

        logger.info(schemaPrinter.print(schema))
        return schema
    }

    @Bean
    fun dataFetcherExceptionHandler(): DataFetcherExceptionHandler =
        CustomDataFetcherExceptionHandler()

    @Bean
    fun graphQL(
        schema: GraphQLSchema,
        dataFetcherExceptionHandler: DataFetcherExceptionHandler
    ): GraphQL = GraphQL.newGraphQL(schema)
        .queryExecutionStrategy(AsyncExecutionStrategy(dataFetcherExceptionHandler))
        .mutationExecutionStrategy(AsyncSerialExecutionStrategy(dataFetcherExceptionHandler))
        .build()
}
