package io.github.zenhelix.web.http.client.jdk

import java.net.http.HttpClient

public fun jdkHttpClient(initializer: HttpClient.Builder.() -> Unit = {}): HttpClient = HttpClient.newBuilder().apply(initializer).build()
