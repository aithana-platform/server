package org.aithana.platform.server.core.impoexpo

import io.github.oshai.kotlinlogging.KotlinLogging
import org.aithana.platform.server.core.model.QuotesTable

private val logger = KotlinLogging.logger {  }

class ImporterLogger(
    private val wrapped: RawDataImporter
): RawDataImporter {

    override fun import(): QuotesTable {
        val table = wrapped.import()

        logger.info {
            "imported:\n" +
            "$table"
        }

        return table
    }
}