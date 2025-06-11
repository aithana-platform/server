package org.aithana.platform.server.presentation.cli.aithana

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import org.aithana.platform.server.application.AithanaBuilder

class BatchCsvInOut: CliktCommand() {
    private val input by option(help = "path to input file").required()
    private val output by option(help = "path to output file").required()

    override fun run() {
        AithanaBuilder()
            .openEncodeUsingGemini()
            .importFromCsv(input)
            .exportToCsv(output)
            .build()
            .process()
    }
}