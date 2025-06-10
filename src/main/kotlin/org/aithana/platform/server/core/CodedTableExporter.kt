package org.aithana.platform.server.core

fun interface CodedTableExporter {
    fun export(table: CodedQuotesTable)
}