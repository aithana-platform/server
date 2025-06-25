package org.aithana.platform.server.steps

import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.mockk.*
import org.aithana.platform.server.application.AithanaBuilder
import org.aithana.platform.server.application.impoexpo.Csv
import java.io.FileWriter
import java.io.Writer
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProcessProjectSteps {

    private val builder = AithanaBuilder()
    private val mockWriter: Writer = mockk<FileWriter>(relaxed = true)

    private lateinit var thrownException: Exception
    private lateinit var writeSlot: CapturingSlot<String>

    @Before
    fun setupMock() {
        writeSlot = slot<String>()
        every { mockWriter.write(capture(writeSlot)) } returns Unit
    }

    @Given("the {string} CSV file")
    fun basicSetup(fileName: String) {
        this.builder
            .openEncodeUsingGemini()
            .importFromCsv(fileName)
            .exportToCsv(mockWriter)
    }

    @Given("the {string} project context file")
    fun setupProjectContextFile(fileName: String) {
        this.builder
            .setProjectContextFile(fileName)
    }

    @When("I ask to process")
    fun runCatchingAllExceptions() {
        try {
            this.builder
                .build()
                .process()
        } catch (e: Exception) {
            this.thrownException = e
        }
    }

    @Then("I get no errors")
    fun thrownExceptionNotInitialized() {
        assertFalse { this::thrownException.isInitialized }
    }

    @Then("I get {string} error")
    fun thrownExceptionMatchesGiven(exceptionName: String) {
        assertTrue { this::thrownException.isInitialized }
        assertEquals(exceptionName, this.thrownException::class.simpleName)
    }

    @Then("no results are written")
    fun mockWriterNotCalled() {
        assertFalse { writeSlot.isCaptured }
    }

    @Then("I get the results in a file")
    fun verifyWellFormedCsvOutput() {
        assertTrue { writeSlot.isCaptured }

        val csvContent = writeSlot.captured

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