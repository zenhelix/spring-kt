package io.github.zenhelix.web.http.client.netty.reactor

import reactor.netty.http.client.HttpClient

public object NettyReactorHttpClientFactory {

    public fun nettyReactorHttpClient(properties: NettyReactorHttpClientProperties, initializer: HttpClientBuilder.() -> Unit = {}): HttpClient =
        nettyReactorHttpClient {
        properties.baseUrl?.also { this.baseUrl(it) }
        properties.compress?.also { this.compress(it) }
        properties.disableRetry?.also { this.disableRetry(it) }
        properties.followRedirect?.also { this.followRedirect(it) }
        properties.keepAlive?.also { this.keepAlive(it) }
        properties.connectTimeout?.also { this.connectTimeout(it) }
        properties.readTimeout?.also { this.readTimeout(it) }
        properties.writeTimeout?.also { this.writeTimeout(it) }
        properties.ignoreSsl?.also { this.ignoreSsl(it) }
        if (properties.useProxy) {
            properties.proxy?.also { this.proxy(it) }
        }
        this.apply(initializer)
    }

}