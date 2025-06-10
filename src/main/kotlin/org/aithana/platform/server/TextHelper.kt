package org.aithana.platform.server

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
}