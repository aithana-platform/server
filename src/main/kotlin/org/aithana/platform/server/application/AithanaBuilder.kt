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
import org.aithana.platform.server.impoexpo.CsvExporter
import org.aithana.platform.server.impoexpo.CsvImporter
import java.io.FileReader
import java.io.FileWriter
import java.io.Writer

class AithanaBuilder {
    companion object {
        private const val ALLOWED_RATE: Double = 1/6.0
    }

    private lateinit var exporter: CodedTableExporter
    private lateinit var importer: RawDataImporter
    private lateinit var coder: Coder

    fun openEncodeUsingGemini(): AithanaBuilder {
        val resourcesLoader = BaseResourcesLoader()
        val concreteCoder = OpenCoder(resourcesLoader)
        this.coder = RateLimiter(concreteCoder, ALLOWED_RATE)
        this.coder = CoderLogger(this.coder)
        return this
    }

    fun encodeUsingCustomCoder(custom: Coder): AithanaBuilder {
        this.coder = CoderLogger(custom)
        return this
    }

    fun importFromCsv(fileName: String): AithanaBuilder {
        val reader = FileReader(fileName)
        val concreteImporter = CsvImporter(reader)
        this.importer = ImporterLogger(concreteImporter)
        return this
    }

    fun customImporter(custom: RawDataImporter): AithanaBuilder {
        this.importer = ImporterLogger(custom)
        return this
    }

    fun exportToCsv(fileName: String): AithanaBuilder {
        val writer = FileWriter(fileName)
        return this.exportToCsv(writer)
    }

    fun exportToCsv(writer: Writer): AithanaBuilder {
        val concreteExporter = CsvExporter(writer)
        this.exporter = ExporterLogger(concreteExporter)
        return this
    }

    fun customExporter(custom: CodedTableExporter): AithanaBuilder {
        this.exporter = ExporterLogger(custom)
        return this
    }

    fun build(): Aithana {
        ensureAllPropsInitialized()

        return AithanaImpl(coder, importer, exporter)
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
}