package io.github.zenhelix.spring.web

import org.springframework.http.InvalidMediaTypeException
import org.springframework.http.MediaType
import java.nio.charset.StandardCharsets

public object MediaTypes {
    public val IMAGE_SVG: MediaType = MediaType("image", "svg+xml")
    public const val IMAGE_SVG_VALUE: String = "image/svg+xml"

    public val TEXT_CSV: MediaType = MediaType("text", "csv")
    public const val TEXT_CSV_VALUE: String = "text/csv"

    public val TEXT_CSV_UTF8: MediaType = MediaType(TEXT_CSV, StandardCharsets.UTF_8)
    public const val TEXT_CSV_UTF8_VALUE: String = "text/csv;charset=UTF-8"

    public val APPLICATION_OOXML_SHEET: MediaType = MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public const val APPLICATION_OOXML_SHEET_VALUE: String = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

    public val APPLICATION_MICROSOFT_EXCEL: MediaType = MediaType("application", "vnd.ms-excel")
    public const val APPLICATION_MICROSOFT_EXCEL_VALUE: String = "application/vnd.ms-excel"
}

public object MediaTypeUtils {

    public fun singleMediaType(mediaType: String): MediaType? = MediaType.parseMediaTypes(mediaType.split(",")).singleOrNull()

    public fun parseOrNull(mediaType: String): MediaType? = try {
        MediaType.parseMediaType(mediaType)
    } catch (_: InvalidMediaTypeException) {
        null
    }

}