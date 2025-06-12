package org.aithana.platform.server.core.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

class TextHelper {
    fun replaceVariables(text: String, variables: Map<String, String>): String {
        var base = text

        variables.forEach { variable, value ->
            base = base.replace(variable, value)
        }

        return base
    }

    fun parseJsonAsSetOfString(json: String): Set<String> {
        val mapper = ObjectMapper().registerKotlinModule()
        try {
            return mapper.readValue(json, object : TypeReference<Set<String>>() {})
        } catch (mie: MismatchedInputException) {
            throw TextHelperException(mie.localizedMessage)
        }
    }

    fun printableCell(content: String?): String {
        val maxLength = 12
        val cellTemplate = "[%s]"

        val formattedContent = if (content.isNullOrEmpty()) (0..(maxLength + 3)).joinToString("") { " " }
        else if (content.length > maxLength) content.substring(0..maxLength) + "..."
        else content.padEnd(maxLength + 3, ' ')

        return String.format(cellTemplate, formattedContent)
    }
}