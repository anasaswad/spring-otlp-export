package nl.aaswad.serviceB

import com.fasterxml.jackson.annotation.JsonProperty
import io.micrometer.observation.Observation
import io.micrometer.observation.ObservationRegistry
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.observability.micrometer.Micrometer
import reactor.core.publisher.Mono

@RestController
class RootController(val registry: ObservationRegistry) {
    data class Input(@JsonProperty("name", required = true) val name: String)
    companion object {
        val LOG: Logger = LoggerFactory.getLogger(RootController::class.java)
    }

    @PostMapping
    fun list(@RequestBody input: Input, context: Observation.Context): Mono<Input> {
        registry.currentObservation?.lowCardinalityKeyValue("name", "process()")
        LOG.info("got request: {}", context.name)
        return Mono.just(input)
            .name("process")
            .tag("name-arg", input.name)
            .tap(Micrometer.observation(registry))
    }
}