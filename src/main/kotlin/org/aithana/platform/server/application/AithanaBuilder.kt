package org.aithana.platform.server.application

import org.aithana.platform.server.application.coder.CoderLogger
import org.aithana.platform.server.application.coder.OpenCoder
import org.aithana.platform.server.application.coder.RateLimiter
import org.aithana.platform.server.core.aithana.Aithana
import org.aithana.platform.server.core.aithana.AithanaImpl
import org.aithana.platform.server.core.coder.*
import org.aithana.platform.server.core.impoexpo.CodedTableExporter
import org.aithana.platform.server.core.impoexpo.ExporterLogger
import org.aithana.platform.server.core.impoexpo.ImporterLogger
import org.aithana.platform.server.core.impoexpo.RawDataImporter
import org.aithana.platform.server.application.impoexpo.CsvExporter
import org.aithana.platform.server.application.impoexpo.CsvImporter
import java.io.FileReader
import java.io.FileWriter
import java.io.Writer

class AithanaBuilder {
    companion object {
        private const val DEFAULT_RATE_LIMIT: Double = 1/6.0
        private const val ENABLED_BY_DEFAULT: Boolean = true
    }

    private lateinit var exporter: CodedTableExporter
    private lateinit var importer: RawDataImporter
    private lateinit var coder: Coder

    private var permitsPerSecond: Double = DEFAULT_RATE_LIMIT
    private var enableLogging: Boolean = ENABLED_BY_DEFAULT

    fun limitEncodingRateTo(permitsPerSecond: Double?): AithanaBuilder {
        this.permitsPerSecond = permitsPerSecond ?: DEFAULT_RATE_LIMIT

        return this
    }

    fun enableLogging() = setLogging(true)
    fun disableLogging() = setLogging(false)

    fun setLogging(enableLogging: Boolean): AithanaBuilder {
        this.enableLogging = enableLogging
        return this
    }

    fun openEncodeUsingGemini(): AithanaBuilder {
        val resourcesLoader = BaseResourcesLoader()
        this.coder = OpenCoder(resourcesLoader)

        return this
    }

    fun encodeUsingCustomCoder(custom: Coder): AithanaBuilder {
        this.coder = custom
        return this
    }

    fun importFromCsv(fileName: String): AithanaBuilder {
        val reader = FileReader(fileName)
        this.importer = CsvImporter(reader)
        return this
    }

    fun customImporter(custom: RawDataImporter): AithanaBuilder {
        this.importer = custom
        return this
    }

    fun exportToCsv(fileName: String): AithanaBuilder {
        val writer = FileWriter(fileName)
        return this.exportToCsv(writer)
    }

    fun exportToCsv(writer: Writer): AithanaBuilder {
        val concreteExporter = CsvExporter(writer)
        this.exporter = concreteExporter
        return this
    }

    fun customExporter(custom: CodedTableExporter): AithanaBuilder {
        this.exporter = custom
        return this
    }

    fun build(): Aithana {
        ensureAllPropsInitialized()

        var finalCoder = this.coder
        var finalImporter = this.importer
        var finalExporter = this.exporter

        if (this.isRateLimited()) {
            finalCoder = RateLimiter(finalCoder, this.permitsPerSecond)
        }

        if (this.enableLogging) {
            finalCoder = CoderLogger(finalCoder)
            finalImporter = ImporterLogger(finalImporter)
            finalExporter = ExporterLogger(finalExporter)
        }

        return AithanaImpl(finalCoder, finalImporter, finalExporter)
    }

    private fun ensureAllPropsInitialized() {
        if (!this::exporter.isInitialized) {
            val interfaceName = CodedTableExporter::class.simpleName.toString()
            throw AithanaBuilderException(interfaceName)
        }

        if (!this::importer.isInitialized) {
            val interfaceName = RawDataImporter::class.simpleName.toString()
            throw AithanaBuilderException(interfaceName)
        }

        if (!this::coder.isInitialized) {
            val interfaceName = Coder::class.simpleName.toString()
            throw AithanaBuilderException(interfaceName)
        }
    }

    private fun isRateLimited(): Boolean = this.permitsPerSecond > 0
}