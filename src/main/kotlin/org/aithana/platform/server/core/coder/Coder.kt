package org.aithana.platform.server.core.coder

fun interface Coder {
    fun code(section: String, quote: String, projectContext: String): Set<String>
}