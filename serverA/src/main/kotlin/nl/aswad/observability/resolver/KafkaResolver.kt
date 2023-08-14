package nl.aswad.observability.resolver

import com.fasterxml.jackson.annotation.JsonProperty
import io.micrometer.tracing.Tracer
import nl.aswad.observability.dto.DataAsset
import nl.aswad.observability.dto.KafkaInput
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class KafkaResolver(
    val tracer: Tracer,
    webClientBuilder: WebClient.Builder,
    @Value("\${service.beta.url}") baseUrl: String
) {

    data class Input(@JsonProperty("name", required = true) val name: String)
    companion object {
        val LOG: Logger = LoggerFactory.getLogger(KafkaResolver::class.java)
    }

    val webClient = webClientBuilder
        .baseUrl(baseUrl)
        .filter { request, next ->
            LOG.info("sending request: {}, headers: {}", request.url(), request.headers())
            next.exchange(request)
        }
        .build()

    fun generateFqn(name: String): Mono<Input> {
        val span = tracer.currentSpan() ?: throw AssertionError("missing span")
        LOG.info("new list request with traceId: {}", span.context().traceId())
        return webClient
            .post()
            .body(BodyInserters.fromValue(Input(name)))
            .retrieve()
            .bodyToMono(Input::class.java)
    }

    fun boostrap(@Suppress("unused") input: KafkaInput): Mono<DataAsset> {
        return generateFqn(input.federation)
            .map { result ->
                DataAsset(
                    result.name,
                    "test description", listOf("1", "2", "4", "5", "9")
                )
            }
    }
}
