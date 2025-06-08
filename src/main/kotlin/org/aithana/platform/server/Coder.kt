package org.aithana.platform.server

typealias Quote = String
typealias Code = String

fun interface Coder {
    fun code(section: String, quote: Quote): Set<Code>
}