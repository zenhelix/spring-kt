package io.github.zenhelix.spring.web.client

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient

//ACCEPT Header Extensions

public fun <T : RestClient.RequestHeadersSpec<T>> RestClient.RequestHeadersSpec<T>.acceptJson(): T = this.accept(MediaType.APPLICATION_JSON)
public fun <T : RestClient.RequestHeadersSpec<T>> RestClient.RequestHeadersSpec<T>.acceptXml(): T = this.accept(MediaType.APPLICATION_XML)
public fun <T : RestClient.RequestHeadersSpec<T>> RestClient.RequestHeadersSpec<T>.acceptOctetStream(): T = this.accept(MediaType.APPLICATION_OCTET_STREAM)
public fun <T : RestClient.RequestHeadersSpec<T>> RestClient.RequestHeadersSpec<T>.acceptPdf(): T = this.accept(MediaType.APPLICATION_PDF)
public fun <T : RestClient.RequestHeadersSpec<T>> RestClient.RequestHeadersSpec<T>.acceptHtml(): T = this.accept(MediaType.TEXT_HTML)
public fun <T : RestClient.RequestHeadersSpec<T>> RestClient.RequestHeadersSpec<T>.acceptTextPlain(): T = this.accept(MediaType.TEXT_PLAIN)

//Other Extensions

public fun <T : RestClient.RequestHeadersSpec<T>> RestClient.RequestHeadersSpec<T>.headers(headers: HttpHeaders? = null): T =
    this.headers { currentHeaders -> headers?.also { currentHeaders.addAll(it) } }