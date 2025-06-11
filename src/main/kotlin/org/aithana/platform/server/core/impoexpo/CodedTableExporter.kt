package org.aithana.platform.server.core.impoexpo

import org.aithana.platform.server.core.model.CodedQuotesTable

fun interface CodedTableExporter {
    fun export(table: CodedQuotesTable)
}