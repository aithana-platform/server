package org.aithana.platform.server.application.impoexpo

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.aithana.platform.server.core.model.CodifiableQuoteCollection
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
        every { mockWriter.flush() } returns Unit

        underTest = CsvExporter(mockWriter)
    }

    @Test
    fun `exporting an empty collection should result in an emtpy Csv`() {
        // given
        val emptyCollection = CodifiableQuoteCollection()

        // when
        underTest.export(emptyCollection)

        // then
        val expectedHeadersOnly = "$EXPECTED_HEADERS\n"
        verify { mockWriter.write(expectedHeadersOnly) }
    }

    @Test
    fun `exporting a collection with a single entry and a single code should result in a Csv with a single line below the headers`() {
        // given
        var collection = CodifiableQuoteCollection()
        val artifactId = "foo"
        val section = "one"
        val quote = "foo bar baz"
        val code = "nonsense"

        collection = collection
            .append(artifactId = artifactId, section = section, quote = quote)
            .let { collection.encode(it) { setOf(code) } }

        // when
        underTest.export(collection)

        // then
        val expected = "$EXPECTED_HEADERS\n" +
                "$artifactId;$section;$quote;$code\n"
        verify { mockWriter.write(expected) }
    }

    @Test
    fun `exporting a table with more than one row should result in a Csv with the equivalent lines below the headers`() {
        // given
        var collection = CodifiableQuoteCollection()
        val artifactId = "foo"
        val section = "one"
        val quote = "foo bar baz"
        val code1 = "nonsense"
        val code2 = "rubbish"

        collection = collection
            .append(artifactId = artifactId, section = section, quote = quote)
            .let { collection.encode(it) { setOf(code1, code2) } }

        // when
        underTest.export(collection)

        // then
        val expected = "$EXPECTED_HEADERS\n" +
                "$artifactId;$section;$quote;$code1\n" +
                "$artifactId;$section;$quote;$code2\n"
        verify { mockWriter.write(expected) }
    }

    @Test
    fun `exporting should call flush`() {
        // given
        val collection = CodifiableQuoteCollection()
        val artifactId = "foo"
        val section = "one"
        val quote = "foo bar baz"
        val code = "nonsense"

        collection
            .append(artifactId = artifactId, section = section, quote = quote)
            .let { collection.encode(it) { setOf(code) } }

        // when
        underTest.export(collection)

        // then
        verify { mockWriter.flush() }
    }
}