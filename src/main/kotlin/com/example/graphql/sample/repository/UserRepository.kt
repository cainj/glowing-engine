package com.example.graphql.sample.repository

import com.example.graphql.sample.model.User
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository :
    CrudRepository<User, String>,
    QuerydslPredicateExecutor<User>
