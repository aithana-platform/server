package org.aithana.platform.server

import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import org.aithana.platform.server.presentation.cli.Aithana
import org.aithana.platform.server.presentation.cli.aithana.BatchCsvInOut

fun main(args: Array<String>) = Aithana()
    .subcommands(BatchCsvInOut())
    .main(args)