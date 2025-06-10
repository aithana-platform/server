package org.aithana.platform.server.core

import java.io.Writer

fun interface CodedTableExporter {
    fun export(table: CodedQuotesTable, writer: Writer)
}