package nl.aswad.observability

import com.fasterxml.jackson.annotation.JsonProperty
import io.micrometer.tracing.Tracer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@RestController
class RootController(
    val tracer: Tracer,
    webClientBuilder: WebClient.Builder,
    @Value("\${service.beta.url}") baseUrl: String
) {
    data class Input(@JsonProperty("name", required = true) val name: String)
    companion object {
        val LOG: Logger = LoggerFactory.getLogger(RootController::class.java)
    }

    val webClient = webClientBuilder
        .baseUrl(baseUrl)
        .filter { request, next ->
            LOG.info("sending request: {}, headers: {}", request.url(), request.headers())
            next.exchange(request)
        }
        .build()

    @GetMapping
    fun list(): Mono<Input> {
        val span = tracer.currentSpan() ?: throw AssertionError("missing span")
        LOG.info("new list request with traceId: {}", span.context().traceId())
        return webClient
            .post()
            .body(BodyInserters.fromValue(Input("test")))
            .retrieve()
            .bodyToMono(Input::class.java)
    }
}