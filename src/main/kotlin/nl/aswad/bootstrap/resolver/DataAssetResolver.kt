package nl.aswad.bootstrap.resolver

import io.micrometer.observation.ObservationRegistry
import nl.aswad.bootstrap.dao.DataAsset
import org.springframework.stereotype.Component
import reactor.core.observability.micrometer.Micrometer
import reactor.core.publisher.Mono

@Component
class DataAssetResolver(val registry: ObservationRegistry) {

    fun find(fqn: String): Mono<DataAsset> {
        return Mono.just<DataAsset>(
                DataAsset(
                        fqn, String.format("auto generated description for %s", fqn), listOf(1, 9))
        )
                .name("dataAsset.find")
                .tag("fqn", fqn)
                .tap(Micrometer.observation(registry))
    }
}
