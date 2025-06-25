package org.aithana.platform.server.application.impoexpo

import org.aithana.platform.server.core.impoexpo.ProjectContextReader
import java.io.FileReader

class TxtContextReader(
    private val fileReader: FileReader
) : ProjectContextReader {
    override fun read(): String = fileReader.readText()
}