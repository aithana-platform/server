package org.aithana.platform.server.core.aithana

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.aithana.platform.server.core.coder.Coder
import org.aithana.platform.server.core.impoexpo.QuotesCollectionExporter
import org.aithana.platform.server.core.impoexpo.ProjectContextReader
import org.aithana.platform.server.core.impoexpo.RawDataImporter
import org.aithana.platform.server.core.model.CodifiableQuoteCollection
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AithanaImplTest {
    @MockK
    private lateinit var mockCoder: Coder
    @MockK
    private lateinit var mockImporter: RawDataImporter
    @MockK
    private lateinit var mockExporter: QuotesCollectionExporter
    @MockK
    private lateinit var mockContextReader: ProjectContextReader

    private lateinit var underTest: AithanaImpl

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)

        underTest = AithanaImpl(
            coder = mockCoder,
            importer = mockImporter,
            exporter = mockExporter,
            contextReader = mockContextReader
        )
    }

    @Test
    fun `it throws when project context is empty`() {
        // given
        val emptyContext = ""
        every { mockContextReader.read() } returns emptyContext

        // when ... then
        assertThrows<EmptyContextException> {
            underTest.process()
        }
    }

    @Test
    fun `it doesn't throw when project context is not empty`() {
        // given
        val notEmptyContext = "foo bar baz"
        every { mockContextReader.read() } returns notEmptyContext
        val collection = CodifiableQuoteCollection().also {
            it.append(artifactId = "foo-bar-baz", quote = "a simple quote", section = "")
        }
        every { mockImporter.import() } returns collection
        every { mockCoder.code(any(), any(), notEmptyContext) } returns setOf("foo", "bar")
        every { mockExporter.export(any()) } returns Unit

        // when ... then
        assertDoesNotThrow {
            underTest.process()
        }
    }

    @Test
    fun `it throws for an empty collection`() {
        // given
        val emptyCollection = CodifiableQuoteCollection()

        // when ... then
        assertThrows<EmptyCollectionException> { underTest.openCode(emptyCollection) }
    }

    @Test
    fun `it adds at least one code for each of the quotes`() {
        // given
        val collection = CodifiableQuoteCollection()
        collection.append(artifactId = "foo-bar-baz", quote = "a simple quote", section = "")
        every { mockCoder.code(any(), any(), any()) } returns setOf("code")

        // when
        val result = underTest.openCode(collection)

        // then
        assertFalse { result.isEmpty() }
        assertTrue { result.codedQuotes().size >= result.uniqueQuotes().size }
    }

    @Test
    fun `it adds more than one code for a complex quote`() {
        // given
        val complexQuote = "a quote that favors this at the expenses of that"
        val collection = CodifiableQuoteCollection()
        collection.append("foo-bar-baz", quote = complexQuote, section = "")
        every { mockCoder.code(any(), complexQuote, any()) } returns setOf("favors this", "costs that")

        // when
        val result = underTest.openCode(collection)

        // then
        assertFalse { result.isEmpty() }
        assertTrue { result.codedQuotes().size >= result.uniqueQuotes().size }
    }
}