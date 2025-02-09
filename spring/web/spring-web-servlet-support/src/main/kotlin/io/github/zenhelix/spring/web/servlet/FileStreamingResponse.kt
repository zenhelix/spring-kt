package io.github.zenhelix.spring.web.servlet

import io.github.zenhelix.spring.http.contentDisposition
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

public data class FileStreamingResponse(val file: StreamingResponseBody, val filename: String, val mimeType: MediaType) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FileStreamingResponse) return false

        if (filename != other.filename) return false
        if (mimeType != other.mimeType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = filename.hashCode()
        result = 31 * result + mimeType.hashCode()
        return result
    }

    override fun toString(): String = "FileStreamingResponse(filename='$filename', mimeType=$mimeType)"

}

public fun FileStreamingResponse.toAttachmentResponse(
    status: HttpStatus = HttpStatus.OK,
    fileNameCharset: Charset = StandardCharsets.UTF_8
): ResponseEntity<StreamingResponseBody> = ResponseEntity.status(status)
    .contentDisposition(ContentDisposition.attachment().filename(this.filename, fileNameCharset).build())
    .contentType(this.mimeType)
    .body(this.file)
