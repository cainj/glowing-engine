package com.example.graphql.sample.extension

import com.example.graphql.sample.validation.DataFetcherExecutionValidator
import com.expedia.graphql.DirectiveWiringHelper
import com.expedia.graphql.execution.DataFetcherExecutionPredicate
import com.expedia.graphql.hooks.SchemaGeneratorHooks
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLType
import java.util.*
import javax.validation.Validator
import kotlin.reflect.KType

/**
 * Schema generator hook that adds additional scalar types.
 */
class CustomSchemaGeneratorHooks(
    validator: Validator,
    private val directiveWiringHelper: DirectiveWiringHelper
) : SchemaGeneratorHooks {

    /**
     * Register additional GraphQL scalar types.
     */
    override fun willGenerateGraphQLType(type: KType): GraphQLType? = when (type.classifier) {
        UUID::class -> graphqlUUIDType
        Date::class -> graphqlDateType
        else -> null
    }

    override fun onRewireGraphQLType(type: KType, generatedType: GraphQLType): GraphQLType {
        return directiveWiringHelper.onWire(generatedType)
    }

    override val dataFetcherExecutionPredicate: DataFetcherExecutionPredicate? =
        DataFetcherExecutionValidator(validator)
}

internal val graphqlUUIDType = GraphQLScalarType(
    "UUID",
    "A type representing a formatted java.util.UUID",
    UUIDCoercing
)

private object UUIDCoercing : Coercing<UUID, String> {

    override fun parseValue(input: Any?): UUID = UUID.fromString(
        serialize(
            input
        )
    )
    override fun parseLiteral(input: Any?): UUID? {
        val uuidString = (input as? StringValue)?.value
        return UUID.fromString(uuidString)
    }

    override fun serialize(dataFetcherResult: Any?): String = dataFetcherResult.toString()
}

internal val graphqlDateType = GraphQLScalarType(
    "Date",
    "A type representing a formatted java.util.Date",
    DateCoercing
)

private object DateCoercing : Coercing<Date, String> {
    override fun parseValue(input: Any?): Date = Date()

    override fun parseLiteral(input: Any?): Date? {
        return Date()
    }

    override fun serialize(dataFetcherResult: Any?): String = Date().toString()
}
