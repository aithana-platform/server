package org.aithana.platform.server.application.impoexpo.columnmapper

import org.aithana.platform.server.application.impoexpo.*

class DefaultColumnMapper: ColumnMapper {
    override fun map(row: Row) = MappingRecord(
        artifactId = row[Csv.IndexMapper.ID.index],
        section = row[Csv.IndexMapper.SECTION.index],
        quote = row[Csv.IndexMapper.QUOTE.index]
    )
}