package io.github.zenhelix.spring.boot.configurationprocessor.test

import org.springframework.boot.configurationprocessor.ConfigurationMetadataAnnotationProcessor
import org.springframework.boot.configurationprocessor.metadata.ConfigurationMetadata
import org.springframework.boot.configurationprocessor.metadata.JsonMarshaller
import org.springframework.core.test.tools.Compiled
import org.springframework.core.test.tools.TestCompiler
import java.util.concurrent.atomic.AtomicReference

public object CompiledMetadataReader {
    public const val METADATA_FILE: String = "META-INF/spring-configuration-metadata.json"

    public fun metadata(compiled: Compiled): ConfigurationMetadata? = compiled.classLoader.getResourceAsStream(METADATA_FILE)?.let { inputStream ->
        try {
            JsonMarshaller().read(inputStream)
        } catch (ex: Exception) {
            throw RuntimeException("Failed to read metadata", ex)
        }
    }

    public fun compileMetadata(compiler: TestCompiler): ConfigurationMetadata {
        val thisCompiler = compiler.withProcessors(ConfigurationMetadataAnnotationProcessor())

        val configurationMetadata = AtomicReference<ConfigurationMetadata>()
        thisCompiler.compile { configurationMetadata.set(metadata(it)) }
        return configurationMetadata.get()
    }

}
