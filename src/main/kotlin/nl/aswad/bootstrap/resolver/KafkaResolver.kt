package nl.aswad.bootstrap.resolver

import nl.aswad.bootstrap.dao.DataAsset
import nl.aswad.bootstrap.dao.KafkaInput
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class KafkaResolver {
    fun boostrap(@Suppress("unused") input: KafkaInput): Mono<DataAsset> {
        return Mono.just(
                DataAsset(
                        "test.fqn",
                        "test description", listOf(1, 2, 4, 5, 9))
        )
    }
}
