package io.github.zenhelix.spring.web

import io.github.zenhelix.spring.http.contentDisposition
import io.github.zenhelix.spring.web.client.multipart
import io.github.zenhelix.spring.web.client.partFile
import org.springframework.core.io.InputStreamSource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

public sealed class FilePart(public open val filename: String, public open val mimeType: MediaType) {

    public data class FilePartBytes(
        val file: ByteArray,
        override val filename: String, override val mimeType: MediaType
    ) : FilePart(filename, mimeType) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            if (!super.equals(other)) return false

            other as FilePartBytes

            if (!file.contentEquals(other.file)) return false
            if (filename != other.filename) return false
            if (mimeType != other.mimeType) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + file.contentHashCode()
            result = 31 * result + filename.hashCode()
            result = 31 * result + mimeType.hashCode()
            return result
        }

        override fun toString(): String {
            return "FilePartBytes(size=${file.size}, filename='$filename', mimeType=$mimeType)"
        }
    }

    public data class FilePartStream(
        public val size: Long,
        public val inputStream: InputStream,
        public override val filename: String, public override val mimeType: MediaType
    ) : FilePart(filename, mimeType) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            if (!super.equals(other)) return false

            other as FilePartStream

            if (size != other.size) return false
            if (filename != other.filename) return false
            if (mimeType != other.mimeType) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + size.hashCode()
            result = 31 * result + filename.hashCode()
            result = 31 * result + mimeType.hashCode()
            return result
        }

        override fun toString(): String {
            return "FilePartStream(size=$size, filename='$filename', mimeType=$mimeType)"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FilePart

        if (filename != other.filename) return false
        if (mimeType != other.mimeType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = filename.hashCode()
        result = 31 * result + mimeType.hashCode()
        return result
    }

    override fun toString(): String = "FilePart(filename='$filename', mimeType=$mimeType)"

}

public const val DEFAULT_UNKNOWN_FILENAME: String = "unknown_filename"

public fun FilePart.FilePartBytes.toAttachmentResponse(
    status: HttpStatus = HttpStatus.OK,
    fileNameCharset: Charset = StandardCharsets.UTF_8
): ResponseEntity<ByteArray> = ResponseEntity.status(status)
    .contentDisposition(ContentDisposition.attachment().filename(this.filename, fileNameCharset).build())
    .contentType(this.mimeType)
    .body(this.file)

public fun MultipartFile.toBytesFilePart(
    defaultFilename: String = DEFAULT_UNKNOWN_FILENAME,
    defaultContentType: MediaType = MediaType.ALL
): FilePart.FilePartBytes = FilePart.FilePartBytes(
    filename = originalFilename ?: defaultFilename,
    file = this.bytes,
    mimeType = this.contentType?.let { MediaTypeUtils.parseOrNull(it) } ?: defaultContentType
)

public fun MultipartFile.toStreamFilePart(
    defaultFilename: String = DEFAULT_UNKNOWN_FILENAME,
    defaultContentType: MediaType = MediaType.ALL
): FilePart.FilePartStream = FilePart.FilePartStream(
    filename = originalFilename ?: defaultFilename,
    size = this.size,
    inputStream = this.inputStream,
    mimeType = this.contentType?.let { MediaTypeUtils.parseOrNull(it) } ?: defaultContentType
)

public fun FilePart.toSingleMultipart(partName: String): MultiValueMap<String, HttpEntity<*>> = multipart { partFile(partName, this@toSingleMultipart) }

public fun <S : InputStreamSource> ResponseEntity<S>.toBytesFilePart(
    defaultFilename: String = DEFAULT_UNKNOWN_FILENAME,
    defaultContentType: MediaType = MediaType.ALL
): FilePart.FilePartBytes = FilePart.FilePartBytes(
    filename = this.headers.contentDisposition.filename ?: defaultFilename,
    file = this.body?.inputStream?.readAllBytes() ?: ByteArray(0),
    mimeType = this.headers.contentType ?: defaultContentType
)
