package io.github.zenhelix.web.http.client.jetty

import org.eclipse.jetty.client.HttpClient

public fun jettyHttpClient(initializer: HttpClientBuilder.() -> Unit = {}): HttpClient = HttpClientBuilder().apply(initializer).build()
