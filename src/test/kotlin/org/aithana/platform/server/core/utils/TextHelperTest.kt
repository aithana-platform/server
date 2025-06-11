package org.aithana.platform.server.core.utils

import org.junit.jupiter.api.Assertions.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class TextHelperTest {
    private lateinit var underTest: TextHelper

    @BeforeTest
    fun setup() {
        underTest = TextHelper()
    }

    @Test
    fun `malformed json throws`() {
        // given
        val malformedJson = ""

        // when ... then
        org.junit.jupiter.api.assertThrows<TextHelperException> {
            underTest.parseJsonAsSetOfString(malformedJson)
        }
    }

    @Test
    fun `empty json array returns empty set`() {
        // given
        val emptyJson = "[]"

        // when
        val result = underTest.parseJsonAsSetOfString(emptyJson)

        // then
        assertIterableEquals(emptySet<String>(), result)
    }

    @Test
    fun `regular json array returns equivalent set`() {
        // given
        val json = "[\"foo\", \"bar\"]"
        val expectedSet = setOf("foo", "bar")

        // when
        val result = underTest.parseJsonAsSetOfString(json)

        // then
        assertIterableEquals(
            expectedSet.toList().sorted(),
            result.toList().sorted()
        )
    }

    @Test
    fun `it does nothing when template has no variables`() {
        // given
        val template = "foo bar baz"
        val variables = mapOf<String,String>()

        // when
        val result = underTest.replaceVariables(template, variables)

        // then
        assertEquals(template, result)
    }

    @Test
    fun `it only replace given variables`() {
        // given
        val template = "foo %VARIABLE_ONE% baz %VARIABLE_TWO%"
        val variables = mapOf(
            "%VARIABLE_ONE%" to "bar"
        )
        val expected = "foo bar baz %VARIABLE_TWO%"

        // when
        val result = underTest.replaceVariables(template, variables)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `it does not throw when there are more variables`() {
        // given
        val template = "foo bar baz"
        val variables = mapOf(
            "%VARIABLE_ONE%" to "bar"
        )

        // when
        val result = underTest.replaceVariables(template, variables)

        // then
        assertEquals(template, result)
    }
}