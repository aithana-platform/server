package org.aithana.platform.server

fun interface ResourcesLoader {
    fun loadFile(filename: String): String
}