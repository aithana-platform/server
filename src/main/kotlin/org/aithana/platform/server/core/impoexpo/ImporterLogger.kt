package org.aithana.platform.server.core.impoexpo

import io.github.oshai.kotlinlogging.KotlinLogging
import org.aithana.platform.server.core.model.CodifiableQuoteCollection

private val logger = KotlinLogging.logger {  }

class ImporterLogger(
    private val wrapped: RawDataImporter
): RawDataImporter {

    override fun import(): CodifiableQuoteCollection {
        val collection = wrapped.import()

        logger.info {
            "imported:\n" +
            "$collection"
        }

        return collection
    }
}