package com.example.graphql.sample.validation

import com.example.graphql.sample.exceptions.ValidationException
import com.example.graphql.sample.exceptions.asConstraintError
import com.expedia.graphql.execution.DataFetcherExecutionPredicate
import graphql.schema.DataFetchingEnvironment
import javax.validation.Valid
import javax.validation.Validator
import kotlin.reflect.KParameter

class DataFetcherExecutionValidator(private val validator: Validator) : DataFetcherExecutionPredicate {

    override fun <T> evaluate(value: T, parameter: KParameter, environment: DataFetchingEnvironment): T {
        val parameterAnnotated = parameter.annotations.any { it.annotationClass == Valid::class }
        val validations = if (parameterAnnotated) {
            validator.validate(value)
        } else {
            emptySet()
        }

        if (validations.isEmpty()) {
            return value
        } else {
            throw ValidationException(validations.map { it.asConstraintError() })
        }
    }
}
