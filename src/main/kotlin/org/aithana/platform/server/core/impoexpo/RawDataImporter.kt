package org.aithana.platform.server.core.impoexpo

import org.aithana.platform.server.core.model.CodifiableQuoteCollection

fun interface RawDataImporter {
    fun import(): CodifiableQuoteCollection
}