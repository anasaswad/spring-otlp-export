package nl.aswad.otlp.logback

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.trace.Span

class OpenTelemetryAppender : AppenderBase<ILoggingEvent>() {
    override fun append(eventObject: ILoggingEvent) {
        val span = Span.current()
        if (!span.spanContext.isValid)
            return
        val builder = Attributes.builder()
        builder.put("loggerName", eventObject.loggerName)
        builder.put("threadName", eventObject.threadName)
        builder.put("level", eventObject.level.toString())
        builder.put("message", eventObject.message)
        if (eventObject.argumentArray != null) {
            eventObject.argumentArray.forEachIndexed { index, item ->
                builder.put("message.argument-$index", item?.toString())
            }
        }
        if (eventObject.throwableProxy != null) {
            builder.put("throwable.message", eventObject.throwableProxy?.message)
            builder.put("throwable.className", eventObject.throwableProxy?.className)
        }
        eventObject.keyValuePairs?.forEach { keyValue -> builder.put(keyValue.key, keyValue.value?.toString()) }

        span.addEvent(eventObject.formattedMessage, builder.build(), eventObject.instant)
    }
}