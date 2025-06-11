package org.aithana.platform.server.application

import org.aithana.platform.server.core.aithana.Aithana
import org.aithana.platform.server.core.aithana.AithanaImpl
import org.aithana.platform.server.core.coder.BaseResourcesLoader
import org.aithana.platform.server.core.coder.Coder
import org.aithana.platform.server.core.coder.OpenCoder
import org.aithana.platform.server.core.impoexpo.CodedTableExporter
import org.aithana.platform.server.core.impoexpo.RawDataImporter
import org.aithana.platform.server.impoexpo.CsvExporter
import org.aithana.platform.server.impoexpo.CsvImporter
import java.io.FileReader
import java.io.FileWriter
import java.io.Writer

class AithanaBuilder {
    private lateinit var exporter: CodedTableExporter
    private lateinit var importer: RawDataImporter
    private lateinit var coder: Coder

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
        this.exporter = CsvExporter(writer)
        return this
    }

    fun customExporter(custom: CodedTableExporter): AithanaBuilder {
        this.exporter = custom
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