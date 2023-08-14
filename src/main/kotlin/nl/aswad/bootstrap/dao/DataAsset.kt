package nl.aswad.bootstrap.dao

@JvmRecord
data class DataAsset(val fqn: String, val description: String, val tags: List<Int>)
