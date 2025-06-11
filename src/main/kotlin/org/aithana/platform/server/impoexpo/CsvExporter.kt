package org.aithana.platform.server.impoexpo

import org.aithana.platform.server.core.model.CodedQuotesTable
import org.aithana.platform.server.core.impoexpo.CodedTableExporter
import java.io.Writer

class CsvExporter(
    private val writer: Writer
): CodedTableExporter {
    override fun export(table: CodedQuotesTable) {
        val nCols = Csv.IndexMapper.entries.size
        val strBuilder = StringBuilder(Csv.OUTPUT_HEADERS.joinToString(Csv.SEPARATOR))
        strBuilder.append("\n")

        val rowFormatter = this.createRowFormatter(strBuilder, nCols)
        table.forEachCodedRow(rowFormatter)

        this.writer.write(strBuilder.toString())
    }

    private fun createRowFormatter(strBuilder: java.lang.StringBuilder, nCols: Int): (String, String, String, String?) -> Unit {
        return { artifactId: String, quote: String, code: String, section: String? ->
            val list: MutableList<String> = MutableList(nCols) { "" }

            list[Csv.IndexMapper.ID.index] = artifactId
            list[Csv.IndexMapper.SECTION.index] = section ?: ""
            list[Csv.IndexMapper.QUOTE.index] = quote
            list[Csv.IndexMapper.CODE.index] = code

            val newLine = list.joinToString(Csv.SEPARATOR) + "\n"
            strBuilder.append(newLine)
        }
    }
}