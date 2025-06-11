package org.aithana.platform.server.core.coder

class ResourcesFileNotFound(filename: String): RuntimeException("File $filename not found in the resources") {
}