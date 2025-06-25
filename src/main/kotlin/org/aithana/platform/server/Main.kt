package org.aithana.platform.server

import com.github.ajalt.clikt.core.main
import org.aithana.platform.server.presentation.cli.Aithana as AithanaCLI

fun main(args: Array<String>) = AithanaCLI().main(args)