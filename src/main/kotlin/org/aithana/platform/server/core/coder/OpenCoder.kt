package org.aithana.platform.server.core.coder

import com.google.genai.Client
import com.google.genai.types.GenerateContentConfig
import com.google.genai.types.Schema
import com.google.genai.types.Type
import org.aithana.platform.server.core.utils.TextHelper

class OpenCoder(
    private val resourcesLoader: ResourcesLoader
): Coder {
    companion object {
        private const val MODEL_VERSION = "gemini-2.0-flash"
        private const val PROMPT_TEMPLATE_FILENAME = "open_coding_prompt.txt"
        private const val RESEARCH_CONTEXT_FILENAME = "ramesses_context.txt"
    }

    private val client: Client = Client()
    private val textHelper = TextHelper()

    override fun code(section: String, quote: String): Set<String> {
        val content = this.client.models.generateContent(
            MODEL_VERSION,
            this.buildPrompt(section, quote),
            this.jsonConfig()
        )

        val emptySetJson = "[]"

        return this.textHelper.parseJsonAsSetOfString(content.text() ?: emptySetJson)
    }

    private fun jsonConfig(): GenerateContentConfig {
        return GenerateContentConfig.builder()
            .responseMimeType("application/json")
            .responseSchema(
                Schema.builder()
                    .type(Type.Known.ARRAY)
                    .items(
                        Schema.builder()
                            .type(Type.Known.STRING)
                            .build()
                    )
                    .build()
            )
            .build()
    }

    private fun buildPrompt(section: String, quote: String): String {
        val template = this.resourcesLoader.loadFile(PROMPT_TEMPLATE_FILENAME)
        val researchContext = this.resourcesLoader.loadFile(RESEARCH_CONTEXT_FILENAME)

        val variables = mapOf(
            "%CONCRETE_RESEARCH_CONTEXT%" to researchContext,
            "%QUOTE_CONTEXT%" to section,
            "%RAW_QUOTE%" to quote
        )

        return this.textHelper.replaceVariables(template, variables)
    }
}