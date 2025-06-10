package org.aithana.platform.server.impoexpo

import org.aithana.platform.server.core.QuotesTable
import org.aithana.platform.server.core.RawDataImporter
import org.aithana.platform.server.core.Table
import java.io.Reader
import java.lang.IndexOutOfBoundsException

class CsvImporter: RawDataImporter {
    companion object {
        private const val CELL_DELIMITER = ";"
        private const val N_HEADER_LINES = 1

        private enum class IndexMapper(val index: Int) {
            ID(0),
            SECTION(1),
            QUOTE(2)
        }
    }

    override fun import(reader: Reader): QuotesTable {
        val content = reader.readText()

        return this.parseFileContent(content)
    }

    private fun splitCells(row: String): List<String> {
        return row.split(CELL_DELIMITER).map { it.trim() }
    }

    private fun createRowProcessor(table: Table): (index: Int, row: List<String>) -> Unit {
        return { index: Int, row: List<String> ->
            try {
                table.append(
                    artifactId = row[IndexMapper.ID.index],
                    section = row[IndexMapper.SECTION.index],
                    quote = row[IndexMapper.QUOTE.index]
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
            .drop(N_HEADER_LINES)
            .forEachIndexed(this.createRowProcessor(table))

        return table
    }
}