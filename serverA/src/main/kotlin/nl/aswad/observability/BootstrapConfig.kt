package nl.aswad.observability

import io.opentelemetry.context.propagation.TextMapPropagator
import nl.aswad.otlp.propagator.CustomTextMapPropagator
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean

@EnableAutoConfiguration
class BootstrapConfig {
    @Bean
    fun customTextMapPropagator(): TextMapPropagator = CustomTextMapPropagator()
}
