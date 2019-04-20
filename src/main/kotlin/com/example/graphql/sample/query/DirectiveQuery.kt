package com.example.graphql.sample.query

import com.example.graphql.sample.directives.SimpleDirective
import org.springframework.stereotype.Component

@Component
class DirectiveQuery : Query {

    @SimpleDirective
    fun directiveFunction(@SimpleDirective value: String) = SimpleDataClass(value)
}

@SimpleDirective
data class SimpleDataClass(

    @SimpleDirective
    val value: String = "goodbye"
)
