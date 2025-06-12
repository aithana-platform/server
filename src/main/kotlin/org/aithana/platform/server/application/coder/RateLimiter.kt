package org.aithana.platform.server.application.coder

import com.google.common.util.concurrent.RateLimiter
import io.github.oshai.kotlinlogging.KotlinLogging
import org.aithana.platform.server.core.coder.Coder

private val logger = KotlinLogging.logger {  }

class RateLimiter(
    private val wrapped: Coder,
    permitsPerSecond: Double
): Coder {
    private val rateLimiter = RateLimiter.create(permitsPerSecond)

    override fun code(section: String, quote: String): Set<String> {
        logger.debug { "acquiring permit..." }
        rateLimiter.acquire()
        logger.debug { "permit acquired!" }
        return wrapped.code(section, quote)
    }
}