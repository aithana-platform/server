package org.aithana.platform.server.core.aithana

import org.aithana.platform.server.core.coder.Coder
import org.aithana.platform.server.core.impoexpo.CodedTableExporter
import org.aithana.platform.server.core.impoexpo.ProjectContextReader
import org.aithana.platform.server.core.impoexpo.RawDataImporter
import org.aithana.platform.server.core.model.CodedQuotesTable
import org.aithana.platform.server.core.model.QuotesTable
import org.aithana.platform.server.core.model.Table

class AithanaImpl(
    private val coder: Coder,
    private val importer: RawDataImporter,
    private val exporter: CodedTableExporter,
    private val contextReader: ProjectContextReader? = null
) : Aithana {
    fun openCode(table: QuotesTable, projectContext: String = ""): CodedQuotesTable {
        if (table.isEmpty())
            throw EmptyTableException()

        val codedTable: CodedQuotesTable = Table()

        table.forEachRow { artifactId, quote, section ->
            this.coder
                .code(section = section ?: "", quote, projectContext)
                .forEach { code ->
                    codedTable.append(code, artifactId, quote, section)
                }
        }

        return codedTable
    }

    override fun process() {
        val projectContext = this.getSafeContext()

        val quotesTable = importer.import()
        val codedTable = this.openCode(quotesTable, projectContext)
        exporter.export(codedTable)
    }

    private fun getSafeContext(): String {
        val context = this.contextReader?.read() ?: ""

        if (context.isBlank())
            throw EmptyContextException()

        return context
    }
}