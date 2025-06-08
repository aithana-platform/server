package org.aithana.platform.server

class Aithana(private val coder: Coder) {
    fun openCode(table: QuotesTable): CodedQuotesTable {
        if (table.isEmpty())
            throw EmptyTableException()

        val codedTable: CodedQuotesTable = Table()

        table.forEachRow { artifactId, quote, section ->
            this.coder
                .code(section = section ?: "", quote)
                .forEach { code ->
                    codedTable.append(code, artifactId, quote, section)
                }
        }

        return codedTable
    }
}