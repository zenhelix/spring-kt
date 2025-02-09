package io.github.zenhelix.spring.webflux.client

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

//ACCEPT Header Extensions

public fun <T : WebClient.RequestHeadersSpec<T>> WebClient.RequestHeadersSpec<T>.acceptJson(): T = this.accept(MediaType.APPLICATION_JSON)
public fun <T : WebClient.RequestHeadersSpec<T>> WebClient.RequestHeadersSpec<T>.acceptXml(): T = this.accept(MediaType.APPLICATION_XML)
public fun <T : WebClient.RequestHeadersSpec<T>> WebClient.RequestHeadersSpec<T>.acceptOctetStream(): T = this.accept(MediaType.APPLICATION_OCTET_STREAM)
public fun <T : WebClient.RequestHeadersSpec<T>> WebClient.RequestHeadersSpec<T>.acceptPdf(): T = this.accept(MediaType.APPLICATION_PDF)
public fun <T : WebClient.RequestHeadersSpec<T>> WebClient.RequestHeadersSpec<T>.acceptHtml(): T = this.accept(MediaType.TEXT_HTML)
public fun <T : WebClient.RequestHeadersSpec<T>> WebClient.RequestHeadersSpec<T>.acceptTextPlain(): T = this.accept(MediaType.TEXT_PLAIN)

//Other Extensions

public fun <T : WebClient.RequestHeadersSpec<T>> WebClient.RequestHeadersSpec<T>.headers(headers: HttpHeaders? = null): T =
    this.headers { currentHeaders -> headers?.also { currentHeaders.addAll(it) } }