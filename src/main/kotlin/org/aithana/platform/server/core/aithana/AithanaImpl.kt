package org.aithana.platform.server.core.aithana

import org.aithana.platform.server.core.coder.BaseResourcesLoader
import org.aithana.platform.server.core.coder.Coder
import org.aithana.platform.server.core.impoexpo.CodedTableExporter
import org.aithana.platform.server.core.impoexpo.RawDataImporter
import org.aithana.platform.server.core.model.CodedQuotesTable
import org.aithana.platform.server.core.model.QuotesTable
import org.aithana.platform.server.core.model.Table

class AithanaImpl(
    private val coder: Coder,
    private val importer: RawDataImporter,
    private val exporter: CodedTableExporter
) : Aithana {
    companion object {
        private const val CONTEXT_FILENAME = "ramesses_context.txt"
    }

    private var ramessesContext: String = BaseResourcesLoader().loadFile(CONTEXT_FILENAME)

    fun openCode(table: QuotesTable): CodedQuotesTable {
        if (table.isEmpty())
            throw EmptyTableException()

        val codedTable: CodedQuotesTable = Table()

        table.forEachRow { artifactId, quote, section ->
            this.coder
                .code(section = section ?: "", quote, ramessesContext)
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