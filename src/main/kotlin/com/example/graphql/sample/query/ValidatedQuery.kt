package com.example.graphql.sample.query

import javax.validation.Valid
import javax.validation.constraints.Pattern
import org.springframework.stereotype.Component

@Component
class ValidatedQuery : Query {
    fun argumentWithValidation(@Valid arg: TypeWithPattern): String = arg.lowerCaseOnly
}

data class TypeWithPattern(
    @field:Pattern(regexp = "[a-z]*")
    val lowerCaseOnly: String
)
