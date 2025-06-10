package org.aithana.platform.server

import java.io.BufferedReader
import java.io.InputStreamReader

class BaseResourcesLoader: ResourcesLoader {
    override fun loadFile(filename: String): String {
        val classLoader = (Thread.currentThread().contextClassLoader
            ?: this::class.java.classLoader)

        return classLoader.getResourceAsStream(filename)?.use { stream ->
            val reader = BufferedReader(InputStreamReader(stream))
            reader.readText()
        } ?: throw ResourcesFileNotFound(filename)
    }
}