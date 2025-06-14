package org.aithana.platform.server.core.aithana

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.aithana.platform.server.core.coder.Coder
import org.aithana.platform.server.core.impoexpo.CodedTableExporter
import org.aithana.platform.server.core.impoexpo.RawDataImporter
import org.aithana.platform.server.core.model.QuotesTable
import org.aithana.platform.server.core.model.Table
import org.junit.jupiter.api.assertThrows
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AithanaTest {
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
        every { mockCoder.code(any(), any()) } returns setOf("code")

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
        every { mockCoder.code(any(), complexQuote) } returns setOf("favors this", "costs that")

        // when
        val result = underTest.openCode(table)

        // then
        assertFalse { result.isEmpty() }
        assertTrue { result.codes().size >= result.quotes().size }
    }
}