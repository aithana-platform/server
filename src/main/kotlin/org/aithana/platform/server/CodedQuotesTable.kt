package org.aithana.platform.server

interface CodedQuotesTable: QuotesTable {
    fun codes(): Set<String>

    fun append(code: String, artifactId: String, quote: String, section: String? = null)
}