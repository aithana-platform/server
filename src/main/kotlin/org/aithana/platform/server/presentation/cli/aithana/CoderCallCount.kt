package org.aithana.platform.server.presentation.cli.aithana

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import org.aithana.platform.server.application.AithanaBuilder
import org.aithana.platform.server.core.coder.RateCounter

class CoderCallCount: CliktCommand() {
    private val input by option(help = "path to input file").required()
    private val output by option(help = "path to output file").required()

    override fun run() {
        val counter = RateCounter()

        AithanaBuilder()
            .encodeUsingCustomCoder(counter)
            .importFromCsv(input)
            .exportToCsv(output)
            .build()
            .process()

        counter.dump()
    }
}