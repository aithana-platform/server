package org.aithana.platform.server

class ResourcesFileNotFound(filename: String): RuntimeException("File $filename not found in the resources") {
}