package org.aithana.platform.server.impoexpo

import org.junit.jupiter.api.assertThrows
import java.io.FileReader
import kotlin.test.*

class CsvImporterTest {
    companion object {
        private const val CSV_FILES_DIRECTORY = "src/test/resources/mockInputFiles"
    }

    private lateinit var underTest: CsvImporter

    @BeforeTest
    fun setup() {
        underTest = CsvImporter()
    }

    @Test
    fun `importing an empty CSV should result in an empty table`() {
        // given
        val emptyCsvFileName = fullTestCsvFilename("empty")
        val reader = FileReader(emptyCsvFileName)

        // when
        val imported = underTest.import(reader)

        // then
        assertTrue { imported.isEmpty() }
    }

    @Test
    fun `importing a single row CSV should result in a single row table`() {
        // given
        val singleRowWithHeaderCsv = fullTestCsvFilename("single_row")
        val reader = FileReader(singleRowWithHeaderCsv)

        // when
        val imported = underTest.import(reader)

        // then
        assertFalse { imported.isEmpty() }
        assertEquals(1, imported.quotes().size)
    }

    @Test
    fun `importing a CSV with a malformed row should throw indicating in which row`() {
        // given
        val malformedRowCsvFile = fullTestCsvFilename("malformed_row")
        val reader = FileReader(malformedRowCsvFile)

        // when ... then
        val exception = assertThrows<ImportingException> { underTest.import(reader) }
        assertTrue { exception.localizedMessage.contains("row 1") }
    }

    private fun fullTestCsvFilename(name: String): String {
        return "$CSV_FILES_DIRECTORY/$name.csv"
    }
}