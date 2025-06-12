package org.aithana.platform.server.application.coder

import io.github.oshai.kotlinlogging.KotlinLogging
import org.aithana.platform.server.core.coder.Coder

private val logger = KotlinLogging.logger {  }

class RateCounter: Coder {
    companion object {
        private val POOL = "foo bar baz bla blaus nothing ping pong".split(" ")
        private const val MIN_CHOICE = 1
        private const val MAX_CHOICE = 4
    }

    private val callRecords: MutableList<Long> = mutableListOf()

    override fun code(section: String, quote: String): Set<String> {
        registerCall()
        return randomCodes()
    }

    private fun registerCall() {
        val now = System.currentTimeMillis()
        this.callRecords.add(now)
    }

    fun dump() {
        logger.debug {
            "\n" + this.callRecords
                .mapIndexed { index, deltaInMillis -> "- $index: $deltaInMillis" }
                .joinToString("\n")
        }
    }

    private fun randomCodes(): Set<String> {
        val n = (MIN_CHOICE..MAX_CHOICE).random()

        return POOL
            .shuffled()
            .subList(0, n)
            .toSet()
    }
}