package org.aithana.platform.server.core.impoexpo

import org.aithana.platform.server.core.model.QuotesTable

fun interface RawDataImporter {
    fun import(): QuotesTable
}