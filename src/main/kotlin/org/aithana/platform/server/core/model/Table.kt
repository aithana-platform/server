package org.aithana.platform.server.core.model

import org.aithana.platform.server.core.utils.TextHelper

class Table(): CodedQuotesTable {
    private data class Row(
        val artifactId: String,
        val quote: String,
        val section: String? = "",
        val code: String = ""
    ) {
        private val textHelper = TextHelper()

        override fun toString(): String {
            return listOf(artifactId, section, quote, code)
                .joinToString(transform = textHelper::printableCell)
        }
    }

    private val rows: MutableList<Row> = mutableListOf()

    override fun codes(): Set<String> {
        return this.rows
            .map { it.code }
            .toSet()
    }

    override fun append(code: String, artifactId: String, quote: String, section: String?) {
        this.rows.add(
            Row(artifactId, quote, section = section, code = code)
        )
    }

    override fun quotes(): Set<String> {
        return this.rows
            .map { it.quote }
            .toSet()
    }

    override fun append(artifactId: String, quote: String, section: String?) {
        this.rows.add(
            Row(artifactId, quote, section = section)
        )
    }

    override fun isEmpty(): Boolean {
        return this.rows.isEmpty()
    }

    override fun forEachRow(behavior: (artifactId: String, quote: String, section: String?) -> Unit) {
        this.rows.forEach { row -> behavior(row.artifactId, row.quote, row.section) }
    }

    override fun forEachCodedRow(behavior: (artifactId: String, quote: String, code: String, section: String?) -> Unit) {
        this.rows.forEach { row -> behavior(row.artifactId, row.quote, row.code, row.section) }
    }

    override fun toString(): String {
        return ("artifact id, section, quote, code\n") +
                this.rows.joinToString("\n") { it.toString() }
    }
}