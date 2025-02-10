package io.github.zenhelix.spring.webflux.client

import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.ClientResponse

public val ClientResponse.Headers.isJsonContentType: Boolean
    get() = isMediaType(MediaType.APPLICATION_JSON)
public val ClientResponse.isJsonContentType: Boolean
    get() = headers().isJsonContentType

public val ClientResponse.Headers.isXmlContentType: Boolean
    get() = isMediaType(MediaType.APPLICATION_XML)
public val ClientResponse.isXmlContentType: Boolean
    get() = headers().isXmlContentType

public val ClientResponse.Headers.isProblemContentType: Boolean
    get() = isMediaType(MediaType.APPLICATION_PROBLEM_JSON)
public val ClientResponse.isProblemContentType: Boolean
    get() = headers().isProblemContentType

public val ClientResponse.Headers.isOctetStreamContentType: Boolean
    get() = isMediaType(MediaType.APPLICATION_OCTET_STREAM)
public val ClientResponse.isOctetStreamContentType: Boolean
    get() = headers().isOctetStreamContentType

public val ClientResponse.Headers.isPdfContentType: Boolean
    get() = isMediaType(MediaType.APPLICATION_PDF)
public val ClientResponse.isPdfContentType: Boolean
    get() = headers().isPdfContentType

public val ClientResponse.Headers.isTextHtmlContentType: Boolean
    get() = isMediaType(MediaType.TEXT_HTML)
public val ClientResponse.isTextHtmlContentType: Boolean
    get() = headers().isTextHtmlContentType

public val ClientResponse.Headers.isTextPlainContentType: Boolean
    get() = isMediaType(MediaType.TEXT_PLAIN)
public val ClientResponse.isTextPlainContentType: Boolean
    get() = headers().isTextPlainContentType

public fun ClientResponse.Headers.isMediaType(mediaType: MediaType): Boolean =
    contentType().let { if (it.isPresent) it.get() else null }?.isCompatibleWith(mediaType) ?: false