package com.example.graphql.sample.auth

import com.example.graphql.sample.model.User
import com.example.graphql.sample.repository.UserRepository
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User as SpringUser
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class MysqlUserDetailService(val userRepository: UserRepository) : ReactiveUserDetailsService,
    ReactiveUserDetailsPasswordService {

    override fun findByUsername(username: String?): Mono<UserDetails> {
        val user = userRepository.findById(username!!).map { u ->
            SpringUser.withUsername(u.email)
                .password(u.password)
                .roles("USER")
                .build()
        }.orElseThrow {
            Exception("Invalid email or password")
        }
        return Mono.just(user)
    }

    override fun updatePassword(user: UserDetails?, newPassword: String?): Mono<UserDetails> =
        userRepository.save(User(user?.username, user?.password)).run {
            Mono.just(user!!)
        }
}
