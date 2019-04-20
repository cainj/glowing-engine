package com.example.graphql.sample.util

import com.example.graphql.sample.model.User
import java.sql.Timestamp
import java.time.Instant
import java.util.*

fun createUser(
    email: String = getRandomWords(4),
    createdTime: Date? = Timestamp.from(Instant.now())
) = User(
    email = email,
    createdTime = createdTime)
