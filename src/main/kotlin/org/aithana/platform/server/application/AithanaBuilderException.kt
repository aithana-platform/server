package org.aithana.platform.server.application

class AithanaBuilderException(uninitializedPropertyName: String)
    : RuntimeException("$uninitializedPropertyName has not been initialized yet") {
}