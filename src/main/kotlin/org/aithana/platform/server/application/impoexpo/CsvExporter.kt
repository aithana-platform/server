package org.aithana.platform.server.application.impoexpo

import org.aithana.platform.server.application.impoexpo.mapper.DefaultEntryMapper
import org.aithana.platform.server.core.impoexpo.QuotesCollectionExporter
import org.aithana.platform.server.core.model.CodifiableQuoteCollection
import java.io.Writer

class CsvExporter(
    private val writer: Writer,
    private val mapper: EntryMapper = DefaultEntryMapper()
): QuotesCollectionExporter {

    override fun export(codedCollection: CodifiableQuoteCollection) {
        val strBuilder = StringBuilder(Csv.OUTPUT_HEADERS.joinToString(Csv.SEPARATOR))
        strBuilder.append("\n")

        val csvRowFormatter = this.createEntryFormatter(strBuilder)
        codedCollection.eachEntry(csvRowFormatter)

        val content = strBuilder.toString()
        this.writer.write(content)
        this.writer.flush()
    }

    private fun createEntryFormatter(strBuilder: java.lang.StringBuilder): (CodifiableQuoteCollection.Entry) -> Unit {
        return { entry ->
            mapper.map(entry).forEach {
                it.joinToString(Csv.SEPARATOR)
                    .let { line -> "$line\n" }
                    .also(strBuilder::append)
            }
        }
    }
}