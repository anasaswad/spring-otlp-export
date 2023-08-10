package nl.aswad.observability

import io.micrometer.observation.ObservationRegistry
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.observability.micrometer.Micrometer
import reactor.core.publisher.Flux

@RestController
class RootController(val registry: ObservationRegistry) {
    @GetMapping
    fun list(): Flux<String> {
        return Flux.range(1, 100)
                .name("list")
                .tag("start", "1")
                .tag("end", "100")
                .tap(Micrometer.observation(registry))
                .map { i -> "index: $i" }
    }
}