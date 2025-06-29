package org.aithana.platform.server.application.impoexpo.mapper

import org.aithana.platform.server.application.impoexpo.Csv
import org.aithana.platform.server.application.impoexpo.Entry
import org.aithana.platform.server.application.impoexpo.EntryMapper
import org.aithana.platform.server.application.impoexpo.Row

class DefaultEntryMapper: EntryMapper {
    override fun map(entry: Entry): List<Row> {
        return entry.codes
            .map { code ->
                val row = MutableList(Csv.IndexMapper.entries.size) { "" }
                row[Csv.IndexMapper.ID.index] = entry.artifactId
                row[Csv.IndexMapper.SECTION.index] = entry.section
                row[Csv.IndexMapper.QUOTE.index] = entry.quote
                row[Csv.IndexMapper.CODE.index] = code

                row
            }
    }
}