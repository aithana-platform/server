package org.aithana.platform.server.core.impoexpo

import org.aithana.platform.server.core.model.CodedQuotesTable

fun interface QuotesCollectionExporter {
    fun export(table: CodedQuotesTable)
}