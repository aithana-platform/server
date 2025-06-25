package org.aithana.platform.server.presentation.cli.aithana

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import org.aithana.platform.server.application.AithanaBuilder
import org.aithana.platform.server.application.coder.RateCounter

class CoderCallCount: CliktCommand() {
    private val input by option(help = "path to input file").required()
    private val output by option(help = "path to output file").required()
    private val context by option(help = "path to project context file").required()

    override fun run() {
        val counter = RateCounter()

        AithanaBuilder()
            .encodeUsingCustomCoder(counter)
            .disableLogging()
            .limitEncodingRateTo(3/2.0)
            .setProjectContextFile(context)
            .importFromCsv(input)
            .exportToCsv(output)
            .build()
            .process()

        counter.dump()
    }
}