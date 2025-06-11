package org.aithana.platform.server.core.coder

import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BaseResourcesLoaderTest {
    private lateinit var underTest: ResourcesLoader

    @BeforeTest
    fun setup() {
        underTest = BaseResourcesLoader()
    }

    @Test
    fun `it throws when the file does not exist`() {
        // given
        val nonExistingFile = "foo"

        // when ... then
        org.junit.jupiter.api.assertThrows<ResourcesFileNotFound> {
            underTest.loadFile(nonExistingFile)
        }
    }

    @Test
    fun `it does not throw when file exists`() {
        // given
        val existingFile = "existingFile.txt"

        // when ... then
        assertDoesNotThrow { underTest.loadFile(existingFile) }
    }

    @Test
    fun `it returns file content when file exists`() {
        // given
        val existingFile = "existingFile.txt"
        val fileContent = "foo bar baz"

        // when
        val content = underTest.loadFile(existingFile)

        // then
        assertEquals(fileContent, content)
    }
}