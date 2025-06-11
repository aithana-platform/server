package org.aithana.platform.server

import org.aithana.platform.server.application.AithanaBuilder

fun main(args: Array<String>) {
    try {
        val inputFileName = args[0]
        val outputFileName = args[1]

        AithanaBuilder()
            .openEncodeUsingGemini()
            .importFromCsv(inputFileName)
            .exportToCsv(outputFileName)
            .build()
            .process()

    } catch (ioobe: IndexOutOfBoundsException) {
        throw RuntimeException("missing CLI parameter")
    }
}