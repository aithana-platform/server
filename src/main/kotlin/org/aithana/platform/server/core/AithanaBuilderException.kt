package org.aithana.platform.server.core

class AithanaBuilderException(uninitializedPropertyName: String)
    : RuntimeException("$uninitializedPropertyName has not been initialized yet") {
}