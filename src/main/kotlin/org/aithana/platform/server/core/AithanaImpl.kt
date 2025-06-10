package org.aithana.platform.server.core

class AithanaImpl(
    private val coder: Coder,
    private val importer: RawDataImporter,
    private val exporter: CodedTableExporter
) : Aithana {
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

    override fun process() {
        val quotesTable = importer.import()
        val codedTable = this.openCode(quotesTable)
        exporter.export(codedTable)
    }
}