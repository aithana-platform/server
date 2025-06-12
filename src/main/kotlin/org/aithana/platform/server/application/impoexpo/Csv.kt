package org.aithana.platform.server.application.impoexpo

object Csv {
    val INPUT_HEADERS = listOf("artifactId","section","quote")
    val OUTPUT_HEADERS = INPUT_HEADERS + "code"
    const val SEPARATOR = ";"
    const val N_HEADER_LINES = 1

    enum class IndexMapper(val index: Int) {
        ID(0),
        SECTION(1),
        QUOTE(2),
        CODE(3)
    }
}