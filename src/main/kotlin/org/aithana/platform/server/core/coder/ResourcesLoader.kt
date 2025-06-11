package org.aithana.platform.server.core.coder

fun interface ResourcesLoader {
    fun loadFile(filename: String): String
}