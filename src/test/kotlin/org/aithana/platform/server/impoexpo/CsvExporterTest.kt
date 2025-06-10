package org.aithana.platform.server.impoexpo

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.aithana.platform.server.core.Table
import java.io.Writer
import kotlin.test.BeforeTest
import kotlin.test.Test

class CsvExporterTest {
    companion object {
        private const val EXPECTED_HEADERS = "artifactId;section;quote;code"
    }

    @MockK
    private lateinit var mockWriter: Writer
    private lateinit var underTest: CsvExporter

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        every { mockWriter.write(any<String>()) } returns Unit

        underTest = CsvExporter()
    }

    @Test
    fun `exporting an empty table should result in an emtpy Csv`() {
        // given
        val emptyTable = Table()

        // when
        underTest.export(emptyTable, mockWriter)

        // then
        val expectedHeadersOnly = "$EXPECTED_HEADERS\n"
        verify { mockWriter.write(expectedHeadersOnly) }
    }

    @Test
    fun `exporting a table with a single row should result in a Csv with a single line below the headers`() {
        // given
        val table = Table()
        val artifactId = "foo"
        val section = "one"
        val quote = "foo bar baz"
        val code = "nonsense"

        table.append(
            artifactId = artifactId, section = section, quote = quote, code = code
        )

        // when
        underTest.export(table, mockWriter)

        // then
        val expected = "$EXPECTED_HEADERS\n" +
                "$artifactId;$section;$quote;$code\n"
        verify { mockWriter.write(expected) }
    }

    @Test
    fun `exporting a table with more than one row should result in a Csv with the equivalent lines below the headers`() {
        // given
        val table = Table()
        val artifactId = "foo"
        val section = "one"
        val quote = "foo bar baz"
        val code1 = "nonsense"
        val code2 = "rubbish"

        table.append(
            artifactId = artifactId, section = section, quote = quote, code = code1
        )
        table.append(
            artifactId = artifactId, section = section, quote = quote, code = code2
        )

        // when
        underTest.export(table, mockWriter)

        // then
        val expected = "$EXPECTED_HEADERS\n" +
                "$artifactId;$section;$quote;$code1\n" +
                "$artifactId;$section;$quote;$code2\n"
        verify { mockWriter.write(expected) }
    }
}