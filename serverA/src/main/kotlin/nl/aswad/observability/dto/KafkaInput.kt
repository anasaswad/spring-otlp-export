package nl.aswad.observability.dto

@JvmRecord
data class KafkaInput(val federation: String, val topic: String)