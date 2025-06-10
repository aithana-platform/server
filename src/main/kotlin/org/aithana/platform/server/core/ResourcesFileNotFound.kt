package org.aithana.platform.server.core

class ResourcesFileNotFound(filename: String): RuntimeException("File $filename not found in the resources") {
}