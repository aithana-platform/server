package org.aithana.platform.server.application.impoexpo.mapper

import org.aithana.platform.server.application.impoexpo.*

class DefaultRowMapper: RowMapper {
    override fun map(row: Row) = MappingRecord(
        artifactId = row[Csv.IndexMapper.ID.index],
        section = row[Csv.IndexMapper.SECTION.index],
        quote = row[Csv.IndexMapper.QUOTE.index]
    )
}