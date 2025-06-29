package org.aithana.platform.server.application.impoexpo

import org.aithana.platform.server.core.model.CodifiableQuoteCollection

typealias Entry = CodifiableQuoteCollection.Entry

fun interface EntryMapper {
    fun map(entry: Entry): List<Row>
}