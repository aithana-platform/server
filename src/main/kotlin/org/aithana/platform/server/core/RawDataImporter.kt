package org.aithana.platform.server.core

import java.io.Reader

fun interface RawDataImporter {
    fun import(reader: Reader): QuotesTable
}