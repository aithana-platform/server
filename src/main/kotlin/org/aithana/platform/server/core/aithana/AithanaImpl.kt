package org.aithana.platform.server.core.aithana

import org.aithana.platform.server.core.coder.Coder
import org.aithana.platform.server.core.impoexpo.QuotesCollectionExporter
import org.aithana.platform.server.core.impoexpo.ProjectContextReader
import org.aithana.platform.server.core.impoexpo.RawDataImporter
import org.aithana.platform.server.core.model.CodifiableQuoteCollection

class AithanaImpl(
    private val coder: Coder,
    private val importer: RawDataImporter,
    private val exporter: QuotesCollectionExporter,
    private val contextReader: ProjectContextReader
) : Aithana {

    fun openCode(collection: CodifiableQuoteCollection, projectContext: String = ""): CodifiableQuoteCollection {
        if (collection.isEmpty())
            throw EmptyCollectionException()

        return collection.encode { entry ->
            this.coder.code(entry.section, entry.quote, projectContext)
        }
    }

    override fun process() {
        val projectContext = this.getSafeContext()

        val rawCollection = importer.import()
        val codedCollection = this.openCode(rawCollection, projectContext)
        exporter.export(codedCollection)
    }

    private fun getSafeContext(): String {
        val context = this.contextReader.read()

        if (context.isBlank())
            throw EmptyContextException()

        return context
    }
}