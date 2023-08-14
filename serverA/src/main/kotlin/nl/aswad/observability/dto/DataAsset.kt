package nl.aswad.observability.dto

@JvmRecord
data class DataAsset(val fqn: String, val description: String, val tags: List<Int>)
