package org.aithana.platform.server.impoexpo

import org.aithana.platform.server.core.QuotesTable
import org.aithana.platform.server.core.RawDataImporter
import org.aithana.platform.server.core.Table
import java.io.Reader
import java.lang.IndexOutOfBoundsException

class CsvImporter(
    private val reader: Reader
): RawDataImporter {

    override fun import(): QuotesTable {
        val content = this.reader.readText()

        return this.parseFileContent(content)
    }

    private fun splitCells(row: String): List<String> {
        return row.split(Csv.SEPARATOR).map { it.trim() }
    }

    private fun createRowProcessor(table: Table): (index: Int, row: List<String>) -> Unit {
        return { index: Int, row: List<String> ->
            try {
                table.append(
                    artifactId = row[Csv.IndexMapper.ID.index],
                    section = row[Csv.IndexMapper.SECTION.index],
                    quote = row[Csv.IndexMapper.QUOTE.index]
                )
            } catch (_: IndexOutOfBoundsException) {
                val fixedIndex = index + 1
                throw ImportingException("malformed row $fixedIndex")
            }
        }
    }

    private fun parseFileContent(content: String): QuotesTable {
        val table = Table()

        content
            .split("\n")
            .map(::splitCells)
            .drop(Csv.N_HEADER_LINES)
            .forEachIndexed(this.createRowProcessor(table))

        return table
    }
}