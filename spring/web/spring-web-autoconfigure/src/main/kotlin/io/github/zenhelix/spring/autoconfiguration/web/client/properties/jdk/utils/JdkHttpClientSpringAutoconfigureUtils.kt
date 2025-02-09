package io.github.zenhelix.spring.autoconfiguration.web.client.properties.jdk.utils

import io.github.zenhelix.spring.autoconfiguration.web.client.jdk.CustomJdkClientHttpRequestFactoryBuilder
import io.github.zenhelix.spring.autoconfiguration.web.client.properties.jdk.JdkHttpClientProperties
import io.github.zenhelix.web.http.client.jdk.JdkHttpClientFactory.jdkHttpClient
import io.github.zenhelix.web.http.client.jdk.ProxyProperties
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings
import org.springframework.boot.ssl.SslBundle
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.http.client.JdkClientHttpRequestFactory
import org.springframework.http.client.reactive.JdkClientHttpConnector
import org.springframework.http.client.reactive.JdkHttpClientResourceFactory
import java.net.InetSocketAddress.createUnresolved
import java.net.ProxySelector
import java.net.http.HttpClient
import java.util.concurrent.Executor

public object JdkHttpClientSpringAutoconfigureUtils {

    public fun jdkHttpClient(
        properties: JdkHttpClientProperties, sslBundle: SslBundle? = null, executor: Executor? = null, initializer: HttpClient.Builder.() -> Unit = {}
    ): HttpClient = jdkHttpClient(properties.toProperties()) {
        sslBundle?.also { this.sslContext(it.createSslContext()) }
        executor?.also { this.executor(it) }

        apply(initializer)
    }

    public fun jdkClientHttpRequestFactory(
        properties: JdkHttpClientProperties, sslBundle: SslBundle? = null, executor: Executor? = null,
        initializer: HttpClient.Builder.() -> Unit = {}
    ): JdkClientHttpRequestFactory = (
            executor?.let { JdkClientHttpRequestFactory(jdkHttpClient(properties, sslBundle, initializer = initializer), executor) }
                ?: JdkClientHttpRequestFactory(jdkHttpClient(properties, sslBundle))
            ).apply { this.setReadTimeout(properties.readTimeout) }

    public fun jdkClientHttpConnector(
        properties: JdkHttpClientProperties,
        resourceFactory: JdkHttpClientResourceFactory? = null, sslBundle: SslBundle? = null, bufferFactory: DataBufferFactory? = null,
        initializer: HttpClient.Builder.() -> Unit = {}
    ): JdkClientHttpConnector =
        JdkClientHttpConnector(jdkHttpClient(properties, sslBundle = sslBundle, executor = resourceFactory?.executor, initializer = initializer)).apply {
        bufferFactory?.also { this.setBufferFactory(it) }
    }

    public fun jdkClientHttpRequestFactory(
        properties: JdkHttpClientProperties,
        builder: CustomJdkClientHttpRequestFactoryBuilder,
        clientHttpRequestFactorySettings: ClientHttpRequestFactorySettings? = null
    ): JdkClientHttpRequestFactory {
        val clientProperties = properties.toProperties()
        return builder
            .httpClientCustomizer { httpClientBuilder ->
                clientProperties.connectTimeout?.also { httpClientBuilder.connectTimeout(it) }
                clientProperties.redirectType?.also { httpClientBuilder.followRedirects(it) }

                if (clientProperties.useProxy && clientProperties.proxy != null) {
                    httpClientBuilder.proxy(ProxySelector.of(createUnresolved(clientProperties.proxy!!.proxyHost, clientProperties.proxy!!.proxyPort ?: 0)))
                } else if (clientProperties.useProxy) {
                    httpClientBuilder.proxy(ProxySelector.getDefault())
                }
            }
            .apply {
                if (clientHttpRequestFactorySettings != null) {
                    this.settings(clientHttpRequestFactorySettings)
                }
            }
            .build(ClientHttpRequestFactoryBuilder.jdk())
    }

    public fun JdkHttpClientProperties.toProperties(): io.github.zenhelix.web.http.client.jdk.JdkHttpClientProperties =
        io.github.zenhelix.web.http.client.jdk.JdkHttpClientProperties(
        connectTimeout = this.connectTimeout,
        redirectType = this.redirectType,
        useProxy = this.useProxy,
        proxy = this.proxy?.let {
            ProxyProperties(
                proxyHost = it.proxyHost, proxyPort = it.proxyPort,
                useSystem = it.useSystem,
            )
        }
    )

}