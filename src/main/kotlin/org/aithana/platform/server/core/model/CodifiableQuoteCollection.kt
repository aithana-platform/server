package org.aithana.platform.server.core.model

class CodifiableQuoteCollection {
    companion object {
        fun from(quotesTable: QuotesTable): CodifiableQuoteCollection {
            val collection = CodifiableQuoteCollection()

            quotesTable.forEachRow { artifactId, quote, section ->
                collection.append(artifactId, section ?: "", quote)
            }

            return collection
        }

        fun from(codedQuotesTable: CodedQuotesTable): CodifiableQuoteCollection {
            var collection = CodifiableQuoteCollection()

            val quoteToIndex = mutableMapOf<Triple<String,String,String>,Int>()

            codedQuotesTable.forEachCodedRow { artifactId, quote, code, section ->

                val safeSection = section ?: ""
                val context = Triple(artifactId, safeSection, quote)

                val index = quoteToIndex[context]
                    ?: collection
                        .append(artifactId, safeSection, quote)
                        .also { quoteToIndex[context] = it }

                collection = collection.addCodes(index, setOf(code))
                println(collection.entries)
            }

            return collection
        }
    }

    data class Entry(
        val index: Int,
        val artifactId: String,
        val section: String,
        val quote: String,
        val codes: Set<String> = emptySet()
    ) { }

    data class CodedQuote(
        val quote: String,
        val code: String
    ) { }

    private val entries: MutableList<Entry>

    constructor() {
        this.entries = mutableListOf<Entry>()
    }

    private constructor(entries: MutableList<Entry>) {
        this.entries = entries
    }

    private fun mapEntries(mappingRule: (entry: Entry) -> Entry): CodifiableQuoteCollection {
        val encodedEntries = entries
            .map(mappingRule)
            .toMutableList()

        return CodifiableQuoteCollection(encodedEntries)
    }

    private fun mapChangingCodesOfEntry(index: Int, updateRule: (set: Set<String>) -> Set<String>) = mapEntries { entry ->
        if (entry.index != index) return@mapEntries entry

        val newCodes = updateRule(entry.codes)
        entry.copy(codes = newCodes)
    }

    fun encode(index: Int, encodingRule: (entry: Entry) -> Set<String>) = mapEntries { entry ->
        if (entry.index != index) entry
        else entry.copy(codes = encodingRule(entry))
    }

    fun encode(encodingRule: (entry: Entry) -> Set<String>) = mapEntries {
        it.copy(codes = encodingRule(it))
    }

    fun addCodes(index: Int, codes: Set<String>) = mapChangingCodesOfEntry(index) {
        it + codes
    }

    fun removeCode(index: Int, code: String) = mapChangingCodesOfEntry(index) {
        it - code
    }

    fun isEmpty(): Boolean = entries.isEmpty()

    fun append(artifactId: String, section: String, quote: String): Int {
        val index = entries.size
        entries.add(Entry(
            index = index,
            artifactId = artifactId,
            section = section,
            quote = quote
        ))

        return index
    }

    fun uniqueQuotes(): Set<String> = entries
        .map { it.quote }
        .toSet()

    fun codedQuotes(): List<CodedQuote> = entries
        .flatMap { entry ->
            entry.codes.map { code ->
                CodedQuote(entry.quote, code)
            }
        }

    fun eachEntry(operator: (Entry) -> Unit) = entries.forEach(operator)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CodifiableQuoteCollection

        return entries == other.entries
    }

    override fun hashCode(): Int {
        return entries.hashCode()
    }
}