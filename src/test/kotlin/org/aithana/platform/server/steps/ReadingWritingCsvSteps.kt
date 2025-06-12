package org.aithana.platform.server.steps

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.mockk.*
import org.aithana.platform.server.application.AithanaBuilder
import org.aithana.platform.server.application.impoexpo.Csv
import java.io.FileWriter
import java.io.Writer
import kotlin.test.assertTrue

class ReadingWritingCsvSteps {
    private val builder = AithanaBuilder()
    private val writer: Writer = mockk<FileWriter>(relaxed = true)

    @Given("the {string} file with artifact ids, the sections of the artifact, and the text quotes")
    fun setupBuilder(filename: String) {
        this.builder
            .importFromCsv(filename)
            .exportToCsv(writer)
            .openEncodeUsingGemini()
    }

    @When("I ask to run open coding on it")
    fun buildAndRun() {
        this.builder
            .build()
            .process()
    }

    @Then("I get the result file with a new column with the codes for each quote")
    fun verifyWellFormedCsvOutput() {
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