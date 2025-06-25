package org.aithana.platform.server.core.aithana

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.aithana.platform.server.core.coder.Coder
import org.aithana.platform.server.core.impoexpo.CodedTableExporter
import org.aithana.platform.server.core.impoexpo.RawDataImporter
import org.aithana.platform.server.core.model.QuotesTable
import org.aithana.platform.server.core.model.Table
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
    private lateinit var mockExporter: CodedTableExporter

    private lateinit var underTest: AithanaImpl

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)

        underTest = AithanaImpl(mockCoder, mockImporter, mockExporter)
    }

    @Test
    fun `it throws when project context is empty`() {
        // given
        val emptyContext = ""

        // when ... then
        assertThrows<EmptyContextException> {
            underTest.process(emptyContext)
        }
    }

    @Test
    fun `it doesn't throw when project context is not empty`() {
        // given
        val notEmptyContext = "foo bar baz"
        val table = Table()
        table.append("foo-bar-baz", "a simple quote")
        every { mockImporter.import() } returns table
        every { mockCoder.code(any(), any(), notEmptyContext) } returns setOf("foo", "bar")
        every { mockExporter.export(any()) } returns Unit

        // when ... then
        assertDoesNotThrow {
            underTest.process(notEmptyContext)
        }
    }

    @Test
    fun `it throws for an empty table`() {
        // given
        val emptyTable: QuotesTable = Table()

        // when ... then
        assertThrows<EmptyTableException> { underTest.openCode(emptyTable) }
    }

    @Test
    fun `it adds at least one code for each of the quotes`() {
        // given
        val table = Table()
        table.append("foo-bar-baz", "a simple quote")
        every { mockCoder.code(any(), any(), any()) } returns setOf("code")

        // when
        val result = underTest.openCode(table)

        // then
        assertFalse { result.isEmpty() }
        assertTrue { result.codes().size >= result.quotes().size }
    }

    @Test
    fun `it adds more than one code for a complex quote`() {
        // given
        val complexQuote = "a quote that favors this at the expenses of that"
        val table = Table()
        table.append("foo-bar-baz", complexQuote)
        every { mockCoder.code(any(), complexQuote, any()) } returns setOf("favors this", "costs that")

        // when
        val result = underTest.openCode(table)

        // then
        assertFalse { result.isEmpty() }
        assertTrue { result.codes().size >= result.quotes().size }
    }
}