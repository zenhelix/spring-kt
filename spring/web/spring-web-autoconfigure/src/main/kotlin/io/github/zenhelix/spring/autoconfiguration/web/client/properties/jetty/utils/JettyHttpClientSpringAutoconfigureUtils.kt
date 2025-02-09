package io.github.zenhelix.spring.autoconfiguration.web.client.properties.jetty.utils

import io.github.zenhelix.spring.autoconfiguration.web.client.jetty.CustomJettyClientHttpRequestFactoryBuilder
import io.github.zenhelix.spring.autoconfiguration.web.client.properties.jetty.JettyHttpClientProperties
import io.github.zenhelix.web.http.client.jetty.HttpClientBuilder
import io.github.zenhelix.web.http.client.jetty.JettyHttpClientFactory.jettyHttpClient
import io.github.zenhelix.web.http.client.jetty.ProxyProperties
import org.eclipse.jetty.client.HttpClient
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings
import org.springframework.boot.ssl.SslBundle
import org.springframework.core.io.buffer.JettyDataBufferFactory
import org.springframework.http.client.JettyClientHttpRequestFactory
import org.springframework.http.client.reactive.JettyClientHttpConnector
import org.springframework.http.client.reactive.JettyResourceFactory

public object JettyHttpClientSpringAutoconfigureUtils {

    public fun jettyClient(
        properties: JettyHttpClientProperties, resourceFactory: JettyResourceFactory? = null, sslBundle: SslBundle? = null,
        initializer: HttpClientBuilder.() -> Unit = {}
    ): HttpClient = jettyHttpClient(properties.toProperties()) {
        resourceFactory?.executor?.also { this.executor(it) }
        resourceFactory?.byteBufferPool?.also { this.byteBufferPool(it) }
        resourceFactory?.scheduler?.also { this.scheduler(it) }

        sslBundle?.also { this.sslContext(it.createSslContext()) }

        apply(initializer)
    }

    public fun jettyClientRequestFactory(
        properties: JettyHttpClientProperties, sslBundle: SslBundle? = null, initializer: HttpClientBuilder.() -> Unit = {}
    ): JettyClientHttpRequestFactory = JettyClientHttpRequestFactory(jettyClient(properties, sslBundle = sslBundle, initializer = initializer)).apply {
        this.setReadTimeout(properties.readTimeout)
    }

    public fun jettyClientHttpConnector(
        properties: JettyHttpClientProperties,
        resourceFactory: JettyResourceFactory? = null, sslBundle: SslBundle? = null, bufferFactory: JettyDataBufferFactory? = null,
        initializer: HttpClientBuilder.() -> Unit = {}
    ): JettyClientHttpConnector = JettyClientHttpConnector(jettyClient(properties, resourceFactory, sslBundle, initializer), resourceFactory).apply {
        bufferFactory?.also { this.setBufferFactory(it) }
    }

    public fun jettyClientHttpRequestFactory(
        properties: JettyHttpClientProperties,
        builder: CustomJettyClientHttpRequestFactoryBuilder,
        clientHttpRequestFactorySettings: ClientHttpRequestFactorySettings? = null
    ): JettyClientHttpRequestFactory {
        val clientProperties = properties.toProperties()
        return builder
            .apply {
                if (clientHttpRequestFactorySettings != null) {
                    this.settings(clientHttpRequestFactorySettings)
                }
            }
            .build(ClientHttpRequestFactoryBuilder.jetty())
    }

    public fun JettyHttpClientProperties.toProperties(): io.github.zenhelix.web.http.client.jetty.JettyHttpClientProperties =
        io.github.zenhelix.web.http.client.jetty.JettyHttpClientProperties(
        connectTimeout = this.connectTimeout,
        ignoreSsl = this.ignoreSsl,
        useProxy = this.useProxy,
        proxy = this.proxy?.let {
            ProxyProperties(
                proxyHost = it.proxyHost, proxyPort = it.proxyPort,
                useSystem = it.useSystem,
            )
        }
    )

}