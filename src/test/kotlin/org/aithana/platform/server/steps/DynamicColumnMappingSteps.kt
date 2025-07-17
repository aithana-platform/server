package org.aithana.platform.server.steps

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.aithana.platform.server.application.AithanaBuilder
import java.io.FileReader
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DynamicColumnMappingSteps {
    private val builder = AithanaBuilder()
    private lateinit var inputFileName: String
    private lateinit var outputFileName: String
    private lateinit var contextFileName: String
    private lateinit var exception: Exception

    @Given("the input {string} in default structure")
    fun setupInputFile(fileName: String) {
        inputFileName = fileName
        builder.importFromCsv(fileName)
    }

    @Given("the custom input {string}")
    fun setupMappingInputFile(fileName: String) {
        inputFileName = fileName
    }

    @Given("the output {string}")
    fun setupOutputFile(fileName: String) {
        outputFileName = fileName
        builder.exportToCsv(fileName)
    }

    @Given("the context {string}")
    fun setupContextFile(fileName: String) {
        contextFileName = fileName
        builder.setProjectContextFile(fileName)
    }

    @Given("no rate limits")
    fun disableRateLimit() {
        builder.limitEncodingRateTo(100.0/1.0)
    }

    @Given("an echo custom coder")
    fun setupEchoCoder() {
        builder.encodeUsingCustomCoder { _, quote, _ -> setOf(quote) }
    }

    @Given("the mapper {string}")
    fun setupMappingFile(fileName: String) {
        builder.importFromCsvWithCustomColumns(
            csvFileName = inputFileName,
            mappingFileName = fileName
        )
    }

    @When("I run the application")
    fun runCatchingExceptions() {
        try {
            builder
                .build()
                .process()
        } catch (e: Exception) {
            exception = e
        }
    }

    @Then("no exception is raised")
    fun noExceptions() {
        assertFalse { ::exception.isInitialized }
    }

    @Then("I get a {string} error")
    fun checkCapturedException(exceptionName: String) {
        assertEquals(exceptionName, exception::class.simpleName)
    }

    @Then("the output file remains empty")
    fun emptyOutput() {
        assertTrue { isEmpty(outputFileName) }
    }

    private fun readLines(fileName: String): List<String> {
        return FileReader(fileName)
            .readLines()
    }

    private fun isEmpty(fileName: String): Boolean {
        return readLines(fileName).isEmpty()
    }
}