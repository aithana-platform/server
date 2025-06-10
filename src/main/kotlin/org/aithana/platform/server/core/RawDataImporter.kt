package org.aithana.platform.server.core

fun interface RawDataImporter {
    fun import(): QuotesTable
}