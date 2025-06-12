package org.aithana.platform.server.core.coder

import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {  }

class CoderLogger(
    private val wrapped: Coder
): Coder {

    override fun code(section: String, quote: String): Set<String> {
        val codes = wrapped.code(section, quote)

        logger.info {
            val joinedCodes = codes.joinToString(", ")
            "encoded ($section) $quote => $joinedCodes"
        }

        return codes
    }
}