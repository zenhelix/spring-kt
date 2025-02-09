package io.github.zenhelix.spring.web.client

import io.github.zenhelix.spring.web.FilePart
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.util.MultiValueMap
import java.io.InputStream

public fun multipart(initializer: MultipartBodyBuilder.() -> Unit = {}): MultiValueMap<String, HttpEntity<*>> =
    MultipartBodyBuilder().apply(initializer).build()

public fun MultipartBodyBuilder.partFile(partName: String, file: FilePart): MultipartBodyBuilder = apply {
    when (file) {
        is FilePart.FilePartBytes  -> byteArrayPart(partName = partName, file = file.file, filename = file.filename, contentType = file.mimeType)
        is FilePart.FilePartStream -> inputStreamPart(partName = partName, file = file.inputStream, filename = file.filename, contentType = file.mimeType)
    }
}

public fun MultipartBodyBuilder.partText(partName: String, text: String): MultipartBodyBuilder = apply { part(partName, text, MediaType.TEXT_PLAIN) }

public fun MultipartBodyBuilder.byteArrayPart(
    partName: String, file: ByteArray,
    filename: String? = null, contentType: MediaType? = null
): MultipartBodyBuilder.PartBuilder = part(partName, ByteArrayResource(file), contentType).apply { filename?.let { filename(it) } ?: this }

public fun MultipartBodyBuilder.inputStreamPart(
    partName: String, file: InputStream,
    filename: String? = null, contentType: MediaType? = null
): MultipartBodyBuilder.PartBuilder = part(partName, InputStreamResource(file), contentType).apply { filename?.let { filename(it) } ?: this }

public fun MultipartBodyBuilder.jsonPart(partName: String, body: Any): MultipartBodyBuilder.PartBuilder = part(partName, body, MediaType.APPLICATION_JSON)
public fun MultipartBodyBuilder.xmlPart(partName: String, body: Any): MultipartBodyBuilder.PartBuilder = part(partName, body, MediaType.APPLICATION_XML)
