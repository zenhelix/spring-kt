package io.github.zenhelix.web.http.client.jdk

import java.net.InetSocketAddress.createUnresolved
import java.net.ProxySelector
import java.net.http.HttpClient

public object JdkHttpClientFactory {

    public fun jdkHttpClient(properties: JdkHttpClientProperties, initializer: HttpClient.Builder.() -> Unit = {}): HttpClient = jdkHttpClient {

        properties.connectTimeout?.also { this.connectTimeout(it) }
        properties.redirectType?.also { this.followRedirects(it) }

        if (properties.useProxy && properties.proxy != null) {
            this.proxy(ProxySelector.of(createUnresolved(properties.proxy.proxyHost, properties.proxy.proxyPort ?: 0)))
        } else if (properties.useProxy) {
            this.proxy(ProxySelector.getDefault())
        }

        this.apply(initializer)
    }

}
