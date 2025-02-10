package io.github.zenhelix.web.http.client.jetty

import org.eclipse.jetty.client.HttpClient

public object JettyHttpClientFactory {

    public fun jettyHttpClient(properties: JettyHttpClientProperties, initializer: HttpClientBuilder.() -> Unit = {}): HttpClient = jettyHttpClient {
        properties.connectTimeout?.also { connectTimeout(it) }
        properties.idleTimeout?.also { idleTimeout(it) }
        if (properties.useProxy) {
            properties.proxy?.also { this.proxy(it) }
        }
        properties.ignoreSsl?.also { this.ignoreSsl(it) }

        apply(initializer)
    }

}
