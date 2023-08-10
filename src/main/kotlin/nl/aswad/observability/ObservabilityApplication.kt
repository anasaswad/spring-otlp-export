@file:JvmName("ObservabilityApplication")

package nl.aswad.observability

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ObservabilityApp

fun main(args: Array<String>) {
    runApplication<ObservabilityApp>(*args)
}
