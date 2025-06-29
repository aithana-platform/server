package org.aithana.platform.server.application.impoexpo

import org.junit.jupiter.api.assertThrows
import java.io.FileReader
import kotlin.test.*

class CsvImporterTest {
    companion object {
        private const val CSV_FILES_DIRECTORY = "src/test/resources/mockInputFiles"
    }

    private lateinit var underTest: CsvImporter

    private fun setupUnderTestInstance(fileID: String) {
        val fullName = fullTestCsvFilename(fileID)
        val reader = FileReader(fullName)

        this.underTest = CsvImporter(reader)
    }

    @Test
    fun `importing an empty CSV should result in an empty table`() {
        // given
        setupUnderTestInstance("empty")

        // when
        val imported = underTest.import()

        // then
        assertTrue { imported.isEmpty() }
    }

    @Test
    fun `importing a single row CSV should result in a single row table`() {
        // given
        setupUnderTestInstance("single_row")

        // when
        val imported = underTest.import()

        // then
        assertFalse { imported.isEmpty() }
        assertEquals(1, imported.uniqueQuotes().size)
    }

    @Test
    fun `importing a CSV with a malformed row should throw indicating in which row`() {
        // given
        setupUnderTestInstance("malformed_row")

        // when ... then
        val exception = assertThrows<ImportingException> { underTest.import() }
        assertTrue { exception.localizedMessage.contains("row 1") }
    }

    private fun fullTestCsvFilename(name: String): String {
        return "$CSV_FILES_DIRECTORY/$name.csv"
    }
}