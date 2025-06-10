package org.aithana.platform.server

import org.aithana.platform.server.core.Table
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class TableTest {
    @Test
    fun `a new table is empty`() {
        // given
        val emptyTable = Table()

        // when
        val result = emptyTable.isEmpty()

        // then
        assertTrue { result }
    }

    @Test
    fun `a table with a row is not empty`() {
        // given
        val singleRowTable = Table()
        singleRowTable.append(
            artifactId = "foo-bar-baz",
            section = "one",
            quote = "foo bar baz of foo and more baz"
        )

        // when
        val result = singleRowTable.isEmpty()

        // then
        assertFalse { result }
    }

    @Test
    fun `a non-empty table gives its quotes`() {
        // given
        val quote = "someting here"
        val table = Table()
        table.append("foo-bar-baz", quote)

        // when
        val result = table.quotes()

        // then
        assertIterableEquals(listOf(quote), result)
    }

    @Test
    fun `a non-empty coded table gives its codes`() {
        // given
        val code = "someting"
        val table = Table()
        table.append(
            code = code,
            artifactId = "foo-bar-baz",
            quote = "something here"
        )

        // when
        val result = table.codes()

        // then
        assertIterableEquals(listOf(code), result)
    }
}