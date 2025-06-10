package org.aithana.platform.server.core

fun interface ResourcesLoader {
    fun loadFile(filename: String): String
}