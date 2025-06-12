package org.aithana.platform.server.core.impoexpo

import io.github.oshai.kotlinlogging.KotlinLogging
import org.aithana.platform.server.core.model.CodedQuotesTable

private val logger = KotlinLogging.logger {  }

class ExporterLogger(
    private val wrapped: CodedTableExporter
): CodedTableExporter {

    override fun export(table: CodedQuotesTable) {
        logger.info {
            "exporting:\n" +
            "$table"
        }
        wrapped.export(table)
    }
}