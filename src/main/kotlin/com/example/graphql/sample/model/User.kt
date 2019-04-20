package com.example.graphql.sample.model

import com.expedia.graphql.annotations.GraphQLDescription
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users")
@GraphQLDescription("User of the system")
data class User(

    @Id
    @Column(name = "email", nullable = false)
    var email: String? = null,

    @Column(name = "password", nullable = false)
    var password: String? = null,

    @Column(name = "createdTime", columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    var createdTime: Date? = null
)
