package org.aithana.platform.server.steps

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.aithana.platform.server.core.aithana.AithanaImpl
import org.aithana.platform.server.core.coder.Coder
import org.aithana.platform.server.core.impoexpo.QuotesCollectionExporter
import org.aithana.platform.server.core.impoexpo.ProjectContextReader
import org.aithana.platform.server.core.impoexpo.RawDataImporter
import org.aithana.platform.server.core.model.CodifiableQuoteCollection
import kotlin.test.assertTrue

class OpenCodingSteps {
    @MockK
    private lateinit var mockCoder: Coder
    @MockK
    private lateinit var mockImporter: RawDataImporter
    @MockK
    private lateinit var mockExporter: QuotesCollectionExporter
    @MockK
    private lateinit var mockContextReader: ProjectContextReader
    private lateinit var collection: CodifiableQuoteCollection
    private lateinit var codedCollection: CodifiableQuoteCollection

    private val SIMPLEST_QUOTE: String = "The architecture is complex."
    private val SIMPLE_QUOTE: String = "I've already used a similar structure in a project I worked on, " +
            "but there we had approached with AI instead of metadata."
    private val COMPLEX_QUOTE: String = "The complexity of the architecture challenges me, " +
            "and in response I end up being more careful and writing better code."

    @Given("a collection containing the artifact id, the section of the artifact, and the text quote")
    fun table() {
        this.collection = CodifiableQuoteCollection()

        collection.append(
            artifactId = "foo-bar-baz",
            section = "chapter 1",
            quote = SIMPLEST_QUOTE
        )

        collection.append(
            artifactId = "foo-bar-baz",
            section = "chapter 1",
            quote = SIMPLE_QUOTE
        )
    }

    @Given("a single quote with enough content to be coded using more than code")
    fun addContentRichQuoteToTable() {
        this.collection.append(
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
        val aithana = AithanaImpl(mockCoder, mockImporter, mockExporter, mockContextReader)
        this.codedCollection = aithana.openCode(this.collection)
    }

    @Then("I get a copy of the table with a new column with the code for each quote")
    fun codesGetAddedToTable() {
        val codes = this.codedCollection.codedQuotes()
        assertTrue { codes.isNotEmpty() }
    }

    @Then("I get more than one line for the same quote, but with different codes")
    fun quoteReplicatedWithDifferentCodes() {
        val quotes = this.codedCollection.uniqueQuotes()
        val codes = this.codedCollection.codedQuotes()

        assertTrue { quotes.isNotEmpty() }
        assertTrue { codes.isNotEmpty() }
        assertTrue { quotes.size < codes.size }
    }
}