package org.aithana.platform.server.application.impoexpo.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.aithana.platform.server.application.impoexpo.*
import java.io.File

data class ColumnNumber(
    val columnNumber: Int
)

data class ColumnsMapping(
    val artifactId: ColumnNumber,
    val quote: ColumnNumber,
    val section: ColumnNumber
) {
    companion object {
        private const val NATURAL_INDEX_FIX_FACTOR = -1
    }

    fun artifactIdIndex() = artifactId.columnNumber + NATURAL_INDEX_FIX_FACTOR
    fun sectionIndex() = section.columnNumber + NATURAL_INDEX_FIX_FACTOR
    fun quoteIndex() = quote.columnNumber + NATURAL_INDEX_FIX_FACTOR
}

data class Config(
    val columnsMapping: ColumnsMapping
)

class YamlRowMapper(
    private val mappingFileName: String
): RowMapper {
    private lateinit var mapping: ColumnsMapping

    private fun isSet() = ::mapping.isInitialized

    private fun ensureSetup() {
        if (isSet()) return

        val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
        val config = mapper.readValue(File(mappingFileName), Config::class.java)
        mapping = config.columnsMapping
    }

    override fun map(row: Row): MappingRecord = try {
        ensureSetup()

        MappingRecord(
            artifactId = row[mapping.artifactIdIndex()],
            section = row[mapping.sectionIndex()],
            quote = row[mapping.quoteIndex()]
        )
    } catch (mkpe: MissingKotlinParameterException) {
        throw ColumnMappingException(mkpe.localizedMessage)
    }
}