package org.aithana.platform.server.core.model

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class CodifiableQuoteCollectionTest {
    @Test
    fun `a new collection is empty`() {
        // given
        val emptyCollection = CodifiableQuoteCollection()

        // when
        val isEmpty = emptyCollection.isEmpty()

        // then
        assertTrue { isEmpty }
    }

    @Test
    fun `a collection is not empty after appending something`() {
        // given
        val notEmptyCollection = CodifiableQuoteCollection()
        notEmptyCollection.append("id", "chapter", "foo bar baz")

        // when
        val isEmpty = notEmptyCollection.isEmpty()

        // then
        assertFalse { isEmpty }
    }

    @Test
    fun `appending return the index of the entry`() {
        // given
        val collection = CodifiableQuoteCollection()

        // when
        val result = collection.append("foo", "bar", "baz")

        // then
        val firstInZeroBasedIndexing = 0
        assertEquals(firstInZeroBasedIndexing, result)
    }

    @Test
    fun `an empty collection has no unique quotes`() {
        // given
        val emptyCollection = CodifiableQuoteCollection()

        // when
        val uniqueQuotes = emptyCollection.uniqueQuotes()

        // then
        assertTrue { uniqueQuotes.isEmpty() }
    }

    @Test
    fun `a single appended entry results in a single quote`() {
        // given
        val notEmptyCollection = CodifiableQuoteCollection()
        notEmptyCollection.append("id", "chapter", "foo bar baz")

        // when
        val uniqueQuotes = notEmptyCollection.uniqueQuotes()

        // then
        assertFalse { uniqueQuotes.isEmpty() }
    }

    @Test
    fun `an empty collection generates empty when coded`() {
        // given
        val emptyCollection = CodifiableQuoteCollection()

        // when
        val encodedCollection = emptyCollection.encode { _ -> setOf("foo") }

        // then
        assertTrue { emptyCollection.isEmpty() }
        assertTrue { encodedCollection.isEmpty() }
    }

    @Test
    fun `encoding a collection isn't in place`() {
        // given
        val originalCollection = CodifiableQuoteCollection()
        originalCollection.append("1", "a", "foo")

        // when
        val encodedCollection = originalCollection.encode { entry ->
            val fakeCode = "${entry.section} ${entry.quote}"
            setOf(fakeCode)
        }

        // then
        assertFalse { originalCollection == encodedCollection }
    }

    @Test
    fun `encoded collection is queriable for coded quotes`() {
        // given
        val originalCollection = CodifiableQuoteCollection()
        originalCollection.append("1", "a", "foo")

        // when
        val encodedCollection = originalCollection.encode { entry ->
            val fakeCode = "${entry.section} ${entry.quote}"
            setOf(fakeCode, fakeCode.reversed())
        }

        // then
        val uniqueQuotes = encodedCollection.uniqueQuotes()
        val codedQuotes = encodedCollection.codedQuotes()
        assertTrue { codedQuotes.isNotEmpty() }
        assertTrue { uniqueQuotes.size < codedQuotes.size }
    }

    @Test
    fun `a single quote can be encoded`() {
        // given
        val originalCollection = CodifiableQuoteCollection()
        val quoteIndex = originalCollection.append("1", "chap", "foo bar")
        originalCollection.append("1", "chap", "bar foo")

        // when
        val encodedCollection = originalCollection.encode(quoteIndex) { entry -> setOf("baz")}

        // then
        assertEquals(2, originalCollection.uniqueQuotes().size)
        assertTrue { originalCollection.codedQuotes().isEmpty() }

        assertEquals(2, encodedCollection.uniqueQuotes().size)
        assertEquals(1, encodedCollection.codedQuotes().size)
    }

    @Test
    fun `a given quote can receive more codes`() {
        // given
        val quote = "foo"
        val code1 = "bar"
        val originalCollection = CodifiableQuoteCollection()
        val quoteIndex = originalCollection.append("1", "chap", quote)
        var codedCollection = originalCollection.encode(quoteIndex) { _ -> setOf(code1) }

        // when
        val code2 = "baz"
        codedCollection = codedCollection.addCodes(quoteIndex, setOf(code2))

        // then
        val expected = setOf(
            CodifiableQuoteCollection.CodedQuote(quote, code1),
            CodifiableQuoteCollection.CodedQuote(quote, code2)
        )
        val codedQuotes = codedCollection.codedQuotes()
        assertIterableEquals(expected, codedQuotes)
    }

    @Test
    fun `a given code can be removed from a quote with multiple codes`() {
        // given
        val quote = "foo"
        val code = "bar"
        val removedCode = "baz"
        val originalCollection = CodifiableQuoteCollection()
        val quoteIndex = originalCollection.append("1", "chap", quote)
        var codedCollection = originalCollection.encode(quoteIndex) { _ -> setOf(code, removedCode) }

        // when
        codedCollection = codedCollection.removeCode(quoteIndex, removedCode)

        // then
        val expected = setOf(
            CodifiableQuoteCollection.CodedQuote(quote, code)
        )
        val codedQuotes = codedCollection.codedQuotes()
        assertIterableEquals(expected, codedQuotes)
    }

    @Test
    fun `a given code can be removed from a quote with a single code`() {
        // given
        val quote = "foo"
        val removedCode = "baz"
        val originalCollection = CodifiableQuoteCollection()
        val quoteIndex = originalCollection.append("1", "chap", quote)
        var codedCollection = originalCollection.encode(quoteIndex) { _ -> setOf(removedCode) }

        // when
        codedCollection = codedCollection.removeCode(quoteIndex, removedCode)

        // then
        val expected = emptySet<CodifiableQuoteCollection.CodedQuote>()
        val codedQuotes = codedCollection.codedQuotes()
        assertIterableEquals(expected, codedQuotes)
    }
}