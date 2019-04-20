package com.example.graphql.sample.query

import com.expedia.graphql.annotations.GraphQLDescription
import java.util.concurrent.CompletableFuture
import org.springframework.stereotype.Component

/**
 * Example async queries.
 */
@Component
class AsyncQuery : Query {

    @GraphQLDescription(
        "Delays for given amount and then echos the string back." +
                " The default async executor will work with CompletableFuture." +
                " To use other rx frameworks you'll need to install a custom one to handle the types correctly."
    )
    fun delayedEchoUsingCompletableFuture(msg: String, delaySeconds: Int): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        Thread {
            Thread.sleep(delaySeconds * 1000L)
            future.complete(msg)
        }.start()
        return future
    }
}
