package org.aithana.platform.server.core

interface QuotesTable {
    fun quotes(): Set<String>

    fun append(artifactId: String, quote: String, section: String? = null)

    fun isEmpty(): Boolean

    fun forEachRow(behavior: (artifactId: String, quote: String, section: String?) -> Unit)
}