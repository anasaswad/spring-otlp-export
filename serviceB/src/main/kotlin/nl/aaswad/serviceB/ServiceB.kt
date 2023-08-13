@file:JvmName("ServiceB")

package nl.aaswad.serviceB

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.core.publisher.Hooks

@SpringBootApplication
class ServiceBApplication

fun main(args: Array<String>) {
    Hooks.enableAutomaticContextPropagation()
    if (System.getenv("HONEYCOMB_TOKEN") == null) {
        throw AssertionError("HONEYCOMB_TOKEN environment variable is missing")
    }
    runApplication<ServiceBApplication>(*args)
}
