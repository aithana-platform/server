package org.aithana.platform.server.core.model

@Deprecated(
    message = "CodedQuotesTable is deprecated, use CodifiableQuotesCollection",
    replaceWith = ReplaceWith("CodifiableQuotesCollection(...)"),
    level = DeprecationLevel.WARNING
)
interface CodedQuotesTable: QuotesTable {
    fun codes(): Set<String>

    fun append(code: String, artifactId: String, quote: String, section: String? = null)

    fun forEachCodedRow(behavior: (artifactId: String, quote: String, code: String, section: String?) -> Unit)
}