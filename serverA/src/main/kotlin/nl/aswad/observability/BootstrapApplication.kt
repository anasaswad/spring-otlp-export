package nl.aswad.observability

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import reactor.core.publisher.Hooks

@SpringBootApplication
class BootstrapApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(BootstrapApplication::class.java, *args)
        }
    }

    init {
        Hooks.enableAutomaticContextPropagation()
    }
}