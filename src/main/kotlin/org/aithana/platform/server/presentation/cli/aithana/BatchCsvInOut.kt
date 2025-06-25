package org.aithana.platform.server.presentation.cli.aithana

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.UsageError
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import org.aithana.platform.server.application.AithanaBuilder

class BatchCsvInOut: CliktCommand() {
    companion object {
        private const val ENV_VAR_NAME = "GOOGLE_API_KEY"
    }

    private val input by option(help = "path to input file").required()
    private val output by option(help = "path to output file").required()
    private val context by option(help = "path to project context file").required()

    override fun run() {
        ensureGoogleApiKey()

        AithanaBuilder()
            .openEncodeUsingGemini()
            .limitEncodingRateTo(1/5.0)
            .enableLogging()
            .setProjectContextFile(context)
            .importFromCsv(input)
            .exportToCsv(output)
            .build()
            .process()
    }

    private fun ensureGoogleApiKey() {
        val apiKey = System.getenv(ENV_VAR_NAME)

        if (!apiKey.isNullOrBlank()) return

        throw UsageError("Env variable '$ENV_VAR_NAME' must be set")
    }
}