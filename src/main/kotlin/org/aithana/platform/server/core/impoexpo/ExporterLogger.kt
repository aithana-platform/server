package org.aithana.platform.server.core.impoexpo

import io.github.oshai.kotlinlogging.KotlinLogging
import org.aithana.platform.server.core.model.CodifiableQuoteCollection

private val logger = KotlinLogging.logger {  }

class ExporterLogger(
    private val wrapped: QuotesCollectionExporter
): QuotesCollectionExporter {

    override fun export(codedCollection: CodifiableQuoteCollection) {
        logger.info {
            "exporting:\n" +
            "$codedCollection"
        }
        wrapped.export(codedCollection)
    }
}