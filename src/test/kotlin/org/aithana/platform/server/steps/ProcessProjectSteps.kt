package org.aithana.platform.server.steps

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.testing.test
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.aithana.platform.server.application.impoexpo.Csv
import org.aithana.platform.server.presentation.cli.aithana.BatchCsvInOut
import java.io.FileReader
import java.io.FileWriter
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class ProcessProjectSteps {
    companion object {
        private const val OUTPUT_FILE_NAME = "./src/test/resources/mockData/output/mock.csv"
    }

    private lateinit var command: CliktCommand
    private var inputArg: String? = null
    private val outputReader = FileReader(OUTPUT_FILE_NAME)
    private var outputArg: String? = null
    private var contextArg: String? = null
    private var errorCode: Int = 0

    @Before
    fun setup() {
        this.command = BatchCsvInOut()
        clearOutputFile()
    }

    @Given("--input={string}")
    fun basicSetup(fileName: String) {
        inputArg = fileName
    }

    @Given("an output")
    fun mockExport() {
        outputArg = OUTPUT_FILE_NAME
    }

    @Given("--context={string}")
    fun setupProjectContextFile(fileName: String) {
        contextArg = fileName
    }

    @When("I ask to process")
    fun runCatchingErrors() {
        val cliArgs = mutableMapOf<String, String>()

        if (inputArg != null)
            cliArgs["input"] = inputArg!!

        if (outputArg != null)
            cliArgs["output"] = outputArg!!

        if (contextArg != null)
            cliArgs["context"] = contextArg!!

        val result = this.command.test(
            argv = cliArgs
                .map { (k, v) -> "--$k $v" }
                .joinToString(" ")
        )

        errorCode = result.statusCode
        println(result)
    }

    @Then("I get no errors")
    fun thrownExceptionNotInitialized() {
        assertEquals(0, errorCode)
    }

    @Then("I get an error")
    fun errorCodeIsntZero() {
        assertNotEquals(0, errorCode)
    }

    @Then("no results are written")
    fun outputFileEmpty() {
        val outputFileContent = outputReader.readText()

        assertTrue { outputFileContent.isEmpty() }
    }

    @Then("I get the results in a file")
    fun verifyWellFormedCsvOutput() {
        val outputFileContent = outputReader.readText()
        assertFalse { outputFileContent.isEmpty() }

        val sheet = outputFileContent
            .split("\n")
            .filter { row -> row.isNotEmpty() }
            .map { row -> row.split(Csv.SEPARATOR).map { cell -> cell.trim() } }

        val allLinesCorrectLength = sheet.all { row -> row.size == Csv.OUTPUT_HEADERS.size }
        val noEmptyCells = sheet.all { row -> row.all { cell -> cell.isNotEmpty() } }

        assertTrue { allLinesCorrectLength }
        assertTrue { noEmptyCells }
    }

    private fun clearOutputFile() {
        val writer = FileWriter(OUTPUT_FILE_NAME)
        writer.write("")
        writer.flush()
        writer.close()
    }
}