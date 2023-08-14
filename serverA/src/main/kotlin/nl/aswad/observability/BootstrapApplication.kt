package nl.aswad.observability

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationStartingEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
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

    @Bean
    fun applicationStartListener() = ApplicationListener<ApplicationStartingEvent> {
        Hooks.enableAutomaticContextPropagation()
        if (System.getenv("HONEYCOMB_TOKEN") == null) {
            throw AssertionError("HONEYCOMB_TOKEN environment variable is missing")
        }
    }
}