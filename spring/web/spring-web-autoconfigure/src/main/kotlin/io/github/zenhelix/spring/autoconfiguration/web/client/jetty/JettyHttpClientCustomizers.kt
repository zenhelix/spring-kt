package io.github.zenhelix.spring.autoconfiguration.web.client.jetty

import org.eclipse.jetty.client.HttpClient
import org.eclipse.jetty.client.HttpClientTransport
import org.eclipse.jetty.io.ClientConnector
import org.springframework.http.client.JettyClientHttpRequestFactory
import java.util.function.Consumer

public class JettyHttpClientConfigurer(private val customizers: List<Consumer<HttpClient>>? = null) {
    public fun accumulate(): Consumer<HttpClient>? = customizers?.takeIf { it.isNotEmpty() }?.let {
        it.reduce { acc, customizer -> acc.andThen(customizer) }
    }

    public fun configure(builder: HttpClient): HttpClient {
        accumulate()?.accept(builder)
        return builder
    }
}

public class JettyHttpClientTransportConfigurer(private val customizers: List<Consumer<HttpClientTransport>>? = null) {
    public fun accumulate(): Consumer<HttpClientTransport>? = customizers?.takeIf { it.isNotEmpty() }?.let {
        it.reduce { acc, customizer -> acc.andThen(customizer) }
    }

    public fun configure(builder: HttpClientTransport): HttpClientTransport {
        accumulate()?.accept(builder)
        return builder
    }
}

public class JettyClientConnectorConfigurer(private val customizers: List<Consumer<ClientConnector>>? = null) {
    public fun accumulate(): Consumer<ClientConnector>? = customizers?.takeIf { it.isNotEmpty() }?.let {
        it.reduce { acc, customizer -> acc.andThen(customizer) }
    }

    public fun configure(builder: ClientConnector): ClientConnector {
        accumulate()?.accept(builder)
        return builder
    }
}

public class JettyClientHttpRequestFactoryConfigurer(private val customizers: List<Consumer<JettyClientHttpRequestFactory>>? = null) {
    public fun accumulate(): Consumer<JettyClientHttpRequestFactory>? = customizers?.takeIf { it.isNotEmpty() }?.let {
        it.reduce { acc, customizer -> acc.andThen(customizer) }
    }

    public fun configure(builder: JettyClientHttpRequestFactory): JettyClientHttpRequestFactory {
        accumulate()?.accept(builder)
        return builder
    }
}
