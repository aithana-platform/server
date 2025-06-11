package org.aithana.platform.server.presentation.cli

import com.github.ajalt.clikt.core.CliktCommand

class Aithana: CliktCommand() {
    override fun run() {
        echo("Running Aithana from CLI")
    }
}