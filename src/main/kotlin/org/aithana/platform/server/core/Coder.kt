package org.aithana.platform.server.core

fun interface Coder {
    fun code(section: String, quote: String): Set<String>
}