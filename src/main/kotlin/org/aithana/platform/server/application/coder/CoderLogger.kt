package org.aithana.platform.server.application.coder

import io.github.oshai.kotlinlogging.KotlinLogging
import org.aithana.platform.server.core.coder.Coder

private val logger = KotlinLogging.logger {  }

class CoderLogger(
    private val wrapped: Coder
): Coder {

    override fun code(section: String, quote: String, projectContext: String): Set<String> {
        val codes = wrapped.code(section, quote, projectContext)

        logger.info {
            val joinedCodes = codes.joinToString(", ")
            "encoded ($section) $quote => $joinedCodes"
        }

        return codes
    }
}