package com.example.graphql.sample.mutation

import com.example.graphql.sample.model.User
import com.example.graphql.sample.repository.UserRepository
import com.expedia.graphql.annotations.GraphQLDescription
import org.springframework.stereotype.Component

@Component
class UserMutation(private val userRepository: UserRepository) : Mutation {

    @GraphQLDescription("add value to a list and return resulting list")
    fun upsertUser(entry: User): MutableList<User> {
        userRepository.save(entry)
        return userRepository.findAll().toMutableList()
    }
}
