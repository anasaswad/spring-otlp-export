@file:JvmName("ObservabilityApplication")

package nl.aswad.observability

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ObservabilityApp

fun main(args: Array<String>) {
    assert(System.getenv("HONEYCOMB_TOKEN") != null) {
        "HONEYCOMB_TOKEN environment variable is missing"
    }
    runApplication<ObservabilityApp>(*args)
}
