package org.aithana.platform.server.steps

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.aithana.platform.server.core.aithana.AithanaImpl
import org.aithana.platform.server.core.coder.Coder
import org.aithana.platform.server.core.impoexpo.CodedTableExporter
import org.aithana.platform.server.core.impoexpo.RawDataImporter
import org.aithana.platform.server.core.model.CodedQuotesTable
import org.aithana.platform.server.core.model.QuotesTable
import org.aithana.platform.server.core.model.Table
import kotlin.test.assertTrue

class OpenCodingSteps {
    @MockK
    private lateinit var mockCoder: Coder
    @MockK
    private lateinit var mockImporter: RawDataImporter
    @MockK
    private lateinit var mockExporter: CodedTableExporter
    private lateinit var table: QuotesTable
    private lateinit var codedTable: CodedQuotesTable

    private val SIMPLEST_QUOTE: String = "The architecture is complex."
    private val SIMPLE_QUOTE: String = "I've already used a similar structure in a project I worked on, " +
            "but there we had approached with AI instead of metadata."
    private val COMPLEX_QUOTE: String = "The complexity of the architecture challenges me, " +
            "and in response I end up being more careful and writing better code."

    @Given("a table containing the artifact id, the section of the artifact, and the text quote")
    fun table() {
        this.table = Table()

        table.append(
            artifactId = "foo-bar-baz",
            section = "chapter 1",
            quote = SIMPLEST_QUOTE
        )

        table.append(
            artifactId = "foo-bar-baz",
            section = "chapter 1",
            quote = SIMPLE_QUOTE
        )
    }

    @Given("a single quote with enough content to be coded using more than code")
    fun addContentRichQuoteToTable() {
        this.table.append(
            artifactId = "foo-bar-baz",
            section = "chapter 2",
            quote = COMPLEX_QUOTE
        )
    }

    @When("I ask to open code my table")
    fun runForTable() {
        MockKAnnotations.init(this)
        every { mockCoder.code(any(), SIMPLEST_QUOTE, any()) } returns setOf("simplest")
        every { mockCoder.code(any(), SIMPLE_QUOTE, any()) } returns setOf("simple")
        every { mockCoder.code(any(), COMPLEX_QUOTE, any()) } returns setOf("one code", "another code")
        val aithana = AithanaImpl(mockCoder, mockImporter, mockExporter)
        this.codedTable = aithana.openCode(this.table)
    }

    @Then("I get a copy of the table with a new column with the code for each quote")
    fun codesGetAddedToTable() {
        val codes = this.codedTable.codes()
        assertTrue { codes.isNotEmpty() }
    }

    @Then("I get more than one line for the same quote, but with different codes")
    fun quoteReplicatedWithDifferentCodes() {
        val quotes = this.codedTable.quotes()
        val codes = this.codedTable.codes()

        println(quotes)
        println(codes)

        assertTrue { quotes.isNotEmpty() }
        assertTrue { codes.isNotEmpty() }
        assertTrue { quotes.size < codes.size }
    }
}