package io.github.zenhelix.spring.autoconfiguration.web.client.properties.reactor.netty.utils

import io.github.zenhelix.spring.autoconfiguration.web.client.properties.reactor.netty.NettyReactorHttpClientProperties
import io.github.zenhelix.spring.autoconfiguration.web.client.reactor.netty.CustomReactorNettyClientHttpRequestFactoryBuilder
import io.github.zenhelix.web.http.client.netty.reactor.HttpClientBuilder
import io.github.zenhelix.web.http.client.netty.reactor.NettyReactorHttpClientFactory.nettyReactorHttpClient
import io.github.zenhelix.web.http.client.netty.reactor.ProxyProperties
import io.github.zenhelix.web.http.client.netty.reactor.sslContextForClient
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings
import org.springframework.boot.ssl.SslBundle
import org.springframework.boot.ssl.SslOptions
import org.springframework.http.client.ReactorClientHttpRequestFactory
import org.springframework.http.client.ReactorResourceFactory
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import reactor.netty.http.client.HttpClient

public object NettyReactorHttpClientSpringAutoconfigureUtils {

    public fun reactorNettyClient(
        properties: NettyReactorHttpClientProperties, resourceFactory: ReactorResourceFactory? = null, sslBundle: SslBundle? = null,
        initializer: HttpClientBuilder.() -> Unit = {}
    ): HttpClient = nettyReactorHttpClient(properties.toProperties()) {
        if (resourceFactory != null) {
            this.channelResources(resourceFactory.loopResources)
            this.connectionProvider(resourceFactory.connectionProvider)
        }
        if (sslBundle != null) {
            this.sslContext(sslContextForClient {
                keyManager(sslBundle.managers.keyManagerFactory)
                    .trustManager(sslBundle.managers.trustManagerFactory)
                    .ciphers(SslOptions.asSet(sslBundle.options.ciphers))
                    .protocols(*sslBundle.options.enabledProtocols)
            })
        }

        apply(initializer)
    }

    public fun reactorNettyClientRequestFactory(
        properties: NettyReactorHttpClientProperties, resourceFactory: ReactorResourceFactory? = null, sslBundle: SslBundle? = null,
        initializer: HttpClientBuilder.() -> Unit = {}
    ): ReactorClientHttpRequestFactory = if (resourceFactory != null) {
        ReactorClientHttpRequestFactory(resourceFactory) { reactorNettyClient(properties, sslBundle = sslBundle, initializer = initializer) }
    } else {
        ReactorClientHttpRequestFactory(reactorNettyClient(properties, sslBundle = sslBundle, initializer = initializer))
    }

    public fun reactorClientHttpConnector(
        properties: NettyReactorHttpClientProperties, resourceFactory: ReactorResourceFactory? = null, sslBundle: SslBundle? = null,
        initializer: HttpClientBuilder.() -> Unit = {}
    ): ReactorClientHttpConnector = if (resourceFactory != null) {
        ReactorClientHttpConnector(resourceFactory) { reactorNettyClient(properties, sslBundle = sslBundle, initializer = initializer) }
    } else {
        ReactorClientHttpConnector(reactorNettyClient(properties, sslBundle = sslBundle, initializer = initializer))
    }

    public fun reactorClientHttpRequestFactory(
        properties: NettyReactorHttpClientProperties,
        builder: CustomReactorNettyClientHttpRequestFactoryBuilder,
        clientHttpRequestFactorySettings: ClientHttpRequestFactorySettings? = null
    ): ReactorClientHttpRequestFactory {
        val clientProperties = properties.toProperties()
        return builder
            .apply {
                if (clientHttpRequestFactorySettings != null) {
                    this.settings(clientHttpRequestFactorySettings)
                }
            }
            .build(ClientHttpRequestFactoryBuilder.reactor())
    }

    public fun NettyReactorHttpClientProperties.toProperties(): io.github.zenhelix.web.http.client.netty.reactor.NettyReactorHttpClientProperties =
        io.github.zenhelix.web.http.client.netty.reactor.NettyReactorHttpClientProperties(
        baseUrl = this.baseUrl,
        compress = this.compress,
        disableRetry = this.disableRetry,
        followRedirect = this.followRedirect,
        keepAlive = this.keepAlive,
        connectTimeout = this.connectTimeout,
        readTimeout = this.readTimeout,
        writeTimeout = this.writeTimeout,
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