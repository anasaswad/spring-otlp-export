package nl.aswad.otlp.propagator

import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.SpanContext
import io.opentelemetry.api.trace.TraceFlags
import io.opentelemetry.api.trace.TraceState
import io.opentelemetry.context.Context
import io.opentelemetry.context.propagation.TextMapGetter
import io.opentelemetry.context.propagation.TextMapPropagator
import io.opentelemetry.context.propagation.TextMapSetter


class CustomTextMapPropagator : TextMapPropagator {
    override fun fields(): Collection<String> = setOf("custom-traceId", "custom-spanId")

    override fun <C : Any?> inject(context: Context, carrier: C?, setter: TextMapSetter<C>) {
        val spanContext = Span.fromContext(context).spanContext
        if (!spanContext.isValid) {
            return
        }
        setter[carrier, "custom-traceId"] = spanContext.traceId
        setter[carrier, "custom-spanId"] = spanContext.spanId
    }

    override fun <C : Any?> extract(context: Context, carrier: C?, getter: TextMapGetter<C>): Context {
        val traceId = getter[carrier, "custom-traceId"]
            ?: return context
        val spanId = getter[carrier, "custom-spanId"]
            ?: return context
        val spanContext = SpanContext.createFromRemoteParent(
            traceId,
            spanId,
            TraceFlags.getSampled(),
            TraceState.getDefault()
        )
        if (!spanContext.isValid) return context
        return context.with(Span.wrap(spanContext))
    }

}