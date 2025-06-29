package org.aithana.platform.server.application.impoexpo

import org.aithana.platform.server.core.impoexpo.RawDataImporter
import org.aithana.platform.server.core.model.CodifiableQuoteCollection
import java.io.Reader

class CsvImporter(
    private val reader: Reader
): RawDataImporter {

    override fun import(): CodifiableQuoteCollection {
        val content = this.reader.readText()

        return this.parseFileContent(content)
    }

    private fun splitCells(row: String): List<String> {
        return row.split(Csv.SEPARATOR).map { it.trim() }
    }

    private fun append(row: List<String>, to: CodifiableQuoteCollection) {
        to.append(
            artifactId = row[Csv.IndexMapper.ID.index],
            section = row[Csv.IndexMapper.SECTION.index],
            quote = row[Csv.IndexMapper.QUOTE.index]
        )
    }

    private fun parseFileContent(content: String): CodifiableQuoteCollection {
        val collection = CodifiableQuoteCollection()

        content
            .split("\n")
            .map(::splitCells)
            .drop(Csv.N_HEADER_LINES)
            .forEachIndexed { zeroBasedIndex, row ->
                try {
                    this.append(row, to = collection)
                } catch (_: IndexOutOfBoundsException) {
                    val fixedIndex = zeroBasedIndex + 1
                    throw ImportingException("malformed row $fixedIndex")
                }
            }

        return collection
    }
}