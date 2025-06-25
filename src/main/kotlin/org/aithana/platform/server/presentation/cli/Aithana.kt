package org.aithana.platform.server.presentation.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import org.aithana.platform.server.presentation.cli.aithana.BatchCsvInOut
import org.aithana.platform.server.presentation.cli.aithana.CoderCallCount

class Aithana: CliktCommand() {

    init {
        subcommands(BatchCsvInOut(), CoderCallCount())
    }

    override fun run() {
        echo("Running Aithana from CLI")
    }
}