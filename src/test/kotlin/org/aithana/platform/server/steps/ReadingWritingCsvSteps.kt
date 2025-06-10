package org.aithana.platform.server.steps

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.mockk.*
import org.aithana.platform.server.core.*
import org.aithana.platform.server.impoexpo.Csv
import org.aithana.platform.server.impoexpo.CsvExporter
import org.aithana.platform.server.impoexpo.CsvImporter
import java.io.FileReader
import java.io.FileWriter
import java.io.Writer
import kotlin.test.assertTrue

class ReadingWritingCsvSteps {
    private lateinit var importer: RawDataImporter
    private lateinit var exporter: CodedTableExporter
    private lateinit var writer: Writer

    @Given("the {string} file with artifact ids, the sections of the artifact, and the text quotes")
    fun foo(filename: String) {
        val reader = FileReader(filename)
        this.importer = CsvImporter(reader)

        this.writer = mockk<FileWriter>(relaxed = true)
        this.exporter = spyk(CsvExporter(writer))
    }

    @When("I ask to run open coding on it")
    fun fooTwo() {
        val resourcesLoader = BaseResourcesLoader()
        val geminiCoder = OpenCoder(resourcesLoader)
        val aithana = Aithana(
            geminiCoder,
            importer,
            exporter
        )

        aithana.run()
    }

    @Then("I get the result file with a new column with the codes for each quote")
    fun fooThree() {
        val slot = slot<String>()
        verify { writer.write(capture(slot)) }

        val csvContent = slot.captured

        val sheet = csvContent
            .split("\n")
            .filter { row -> row.isNotEmpty() }
            .map { row -> row.split(Csv.SEPARATOR).map { cell -> cell.trim() } }

        val allLinesCorrectLength = sheet.all { row -> row.size == Csv.OUTPUT_HEADERS.size }
        val noEmptyCells = sheet.all { row -> row.all { cell -> cell.isNotEmpty() } }

        assertTrue { allLinesCorrectLength }
        assertTrue { noEmptyCells }
    }
}