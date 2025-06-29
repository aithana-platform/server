package org.aithana.platform.server.application.impoexpo

typealias Row = List<String>

data class MappingRecord(
    val artifactId: String,
    val section: String,
    val quote: String,
) {}

fun interface RowMapper {
    fun map(row: Row): MappingRecord
}