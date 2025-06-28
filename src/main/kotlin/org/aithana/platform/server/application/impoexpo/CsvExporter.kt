package org.aithana.platform.server.application.impoexpo

import org.aithana.platform.server.core.model.CodedQuotesTable
import org.aithana.platform.server.core.impoexpo.QuotesCollectionExporter
import org.aithana.platform.server.core.model.CodifiableQuoteCollection
import java.io.Writer

class CsvExporter(
    private val writer: Writer
): QuotesCollectionExporter {


    //
    //
    //
    // TODO: move up the mapping
    override fun export(table: CodedQuotesTable) =
        export(CodifiableQuoteCollection.from(table))

    fun export(codedCollection: CodifiableQuoteCollection) {
        val nCols = Csv.IndexMapper.entries.size
        val strBuilder = StringBuilder(Csv.OUTPUT_HEADERS.joinToString(Csv.SEPARATOR))
        strBuilder.append("\n")

        val csvRowFormatter = this.createEntryFormatter(strBuilder, nCols)
        codedCollection.eachEntry(csvRowFormatter)

        val content = strBuilder.toString()
        this.writer.write(content)
        this.writer.flush()
    }

    private fun createEntryFormatter(strBuilder: java.lang.StringBuilder, nCols: Int): (CodifiableQuoteCollection.Entry) -> Unit {
        return { entry ->
            println("running for entry: $entry")

            entry.codes.forEach { code ->
                println("running for code: $code")
                val csvLineAsList: MutableList<String> = MutableList(nCols) { "" }

                csvLineAsList[Csv.IndexMapper.ID.index] = entry.artifactId
                csvLineAsList[Csv.IndexMapper.SECTION.index] = entry.section
                csvLineAsList[Csv.IndexMapper.QUOTE.index] = entry.quote
                csvLineAsList[Csv.IndexMapper.CODE.index] = code

                val newCsvLine = csvLineAsList.joinToString(Csv.SEPARATOR) + "\n"
                strBuilder.append(newCsvLine)
            }
        }
    }
}