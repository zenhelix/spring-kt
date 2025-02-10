package io.github.zenhelix.spring.autoconfiguration.web.client.properties.apache.utils

import io.github.zenhelix.spring.autoconfiguration.web.client.apache.ApacheHttpClientBuilderConfigurer
import io.github.zenhelix.spring.autoconfiguration.web.client.apache.CustomHttpComponentsClientHttpRequestFactoryBuilder
import io.github.zenhelix.spring.autoconfiguration.web.client.properties.apache.ApacheHttpAsyncClientProperties
import io.github.zenhelix.spring.autoconfiguration.web.client.properties.apache.ApacheHttpClientProperties
import io.github.zenhelix.web.http.client.apache.ApacheHttpClientFactory.apacheHttpClient
import io.github.zenhelix.web.http.client.apache.ApacheHttpClientFactory.connectionConfig
import io.github.zenhelix.web.http.client.apache.ApacheHttpClientFactory.poolingHttpClientConnectionManager
import io.github.zenhelix.web.http.client.apache.ApacheHttpClientFactory.requestConfig
import io.github.zenhelix.web.http.client.apache.ApacheHttpClientFactory.socketConfig
import io.github.zenhelix.web.http.client.apache.ConnectionConfigProperties
import io.github.zenhelix.web.http.client.apache.PoolingAsyncConnectionManagerProperties
import io.github.zenhelix.web.http.client.apache.PoolingConnectionManagerProperties
import io.github.zenhelix.web.http.client.apache.ProxyProperties
import io.github.zenhelix.web.http.client.apache.RequestConfigProperties
import io.github.zenhelix.web.http.client.apache.SocketConfigProperties
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.ssl.DefaultHostnameVerifier
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory
import org.apache.hc.core5.http.HttpHost
import org.apache.hc.core5.http.io.SocketConfig
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings
import org.springframework.boot.ssl.SslBundle
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import java.util.function.Consumer

public object ApacheHttpClientSpringAutoconfigureUtils {

    public fun apacheHttpClient(
        properties: ApacheHttpClientProperties, sslBundle: SslBundle? = null,
        builder: HttpClientBuilder = HttpClientBuilder.create(), httpClientBuilderConfigurer: ApacheHttpClientBuilderConfigurer? = null
    ): CloseableHttpClient = apacheHttpClient(properties.toProperties(), builder = builder) {
        if (sslBundle != null) {
            properties.toProperties().poolingConnectionManager?.let {
                poolingHttpClientConnectionManager(it) {
                    val options = sslBundle.options
                    val socketFactory =
                        SSLConnectionSocketFactory(sslBundle.createSslContext(), options.enabledProtocols, options.ciphers, DefaultHostnameVerifier())

                    setSSLSocketFactory(socketFactory)
                }
            }?.also { setConnectionManager(it) }
        }

        httpClientBuilderConfigurer?.configure(this)
    }

    public fun apacheHttpClient(
        properties: ApacheHttpClientProperties, sslBundle: SslBundle? = null,
        builder: HttpClientBuilder = HttpClientBuilder.create(), initializer: HttpClientBuilder.() -> Unit = {}
    ): CloseableHttpClient = apacheHttpClient(properties.toProperties(), builder = builder) {
        if (sslBundle != null) {
            properties.toProperties().poolingConnectionManager?.let {
                poolingHttpClientConnectionManager(it) {
                    val options = sslBundle.options
                    val socketFactory =
                        SSLConnectionSocketFactory(sslBundle.createSslContext(), options.enabledProtocols, options.ciphers, DefaultHostnameVerifier())

                    setSSLSocketFactory(socketFactory)
                }
            }?.also { setConnectionManager(it) }
        }

        apply(initializer)
    }

    public fun httpComponentsClientHttpRequestFactory(
        properties: ApacheHttpClientProperties, sslBundle: SslBundle? = null,
        builder: HttpClientBuilder = HttpClientBuilder.create(), httpClientBuilderConfigurer: ApacheHttpClientBuilderConfigurer? = null
    ): HttpComponentsClientHttpRequestFactory =
        HttpComponentsClientHttpRequestFactory(apacheHttpClient(properties, sslBundle, builder, httpClientBuilderConfigurer))

    public fun httpComponentsClientHttpRequestFactory(
        properties: ApacheHttpClientProperties, sslBundle: SslBundle? = null,
        builder: HttpClientBuilder = HttpClientBuilder.create(), initializer: HttpClientBuilder.() -> Unit = {}
    ): HttpComponentsClientHttpRequestFactory = HttpComponentsClientHttpRequestFactory(apacheHttpClient(properties, sslBundle, builder, initializer))

    public fun httpComponentsClientHttpRequestFactory(
        properties: ApacheHttpClientProperties,
        builder: CustomHttpComponentsClientHttpRequestFactoryBuilder,
        clientHttpRequestFactorySettings: ClientHttpRequestFactorySettings? = null
    ): HttpComponentsClientHttpRequestFactory {
        val clientProperties = properties.toProperties()
        return builder
            .apply {
                this.httpClientCustomizer { httpClientBuilder ->
                    clientProperties.connectionManagerShared?.also { httpClientBuilder.setConnectionManagerShared(it) }
                    if (properties.useProxy && properties.proxy != null) {
                        httpClientBuilder.setProxy(HttpHost(properties.proxy.proxyHost, properties.proxy.proxyPort ?: -1))
                    }
                }
            }
            .apply {
                val customizer =
                    clientProperties.poolingConnectionManager?.socketConfig?.let { config -> Consumer<SocketConfig.Builder> { it.socketConfig(config) } }
                customizer?.also { this.socketConfigCustomizer(it) }
            }
            .apply {
                val customizer = clientProperties.defaultRequestConfig?.let { config ->
                    Consumer<RequestConfig.Builder> { it.requestConfig(config) }
                }
                customizer?.also { this.requestConfigCustomizer(it) }
            }
            .apply {
                this.connectionManagerCustomizer { connectionManagerBuilder ->
                    clientProperties.poolingConnectionManager?.maxConnections?.also { connectionManagerBuilder.setMaxConnTotal(it) }
                    clientProperties.poolingConnectionManager?.maxConnectionsPerRoute?.also { connectionManagerBuilder.setMaxConnPerRoute(it) }
                    clientProperties.poolingConnectionManager?.connectionConfig?.let { connectionConfig(it) }
                        ?.also { connectionManagerBuilder.setDefaultConnectionConfig(it) }
                }
            }
            .apply {
                if (clientHttpRequestFactorySettings != null) {
                    this.settings(clientHttpRequestFactorySettings)
                }
            }
            .build(ClientHttpRequestFactoryBuilder.httpComponents())
    }

    public fun ApacheHttpClientProperties.toProperties(): io.github.zenhelix.web.http.client.apache.ApacheHttpClientProperties =
        io.github.zenhelix.web.http.client.apache.ApacheHttpClientProperties(
        connectionManagerShared = this.connectionManagerShared,
        defaultRequestConfig = RequestConfigProperties(
            connectionRequestTimeout = this.request?.connectionRequestTimeout,
            responseTimeout = this.readTimeout,
            connectionKeepAlive = this.request?.connectionKeepAlive,
            redirectsEnabled = this.request?.redirectsEnabled,
            circularRedirectsAllowed = this.request?.circularRedirectsAllowed,
            maxRedirects = this.request?.maxRedirects,
            protocolUpgradeEnabled = this.request?.protocolUpgradeEnabled
        ),

        useProxy = this.useProxy,
        proxy = this.proxy?.let {
            ProxyProperties(
                proxyHost = it.proxyHost, proxyPort = it.proxyPort,
                useSystem = it.useSystem
            )
        },
        poolingConnectionManager = this.pool?.let {
            PoolingConnectionManagerProperties(
                maxConnections = it.maxConnections,
                maxConnectionsPerRoute = it.maxConnectionsPerRoute,
                connectionConfig = ConnectionConfigProperties(
                    connectTimeout = this.connectTimeout,
                    connectionTimeToLive = it.connection?.connectionTimeToLive,
                    validateAfterInactivityTimeout = it.connection?.validateAfterInactivityTimeout,
                    socketTimeout = it.connection?.socketTimeout
                ),
                socketConfig = it.socket?.let {
                    SocketConfigProperties(
                        soTimeout = it.soTimeout,
                        soReuseAddress = it.soReuseAddress,
                        soKeepAlive = it.soKeepAlive,
                        tcpNoDelay = it.tcpNoDelay,
                        sndBufSize = it.sndBufSize,
                        rcvBufSize = it.rcvBufSize,
                        backlogSize = it.backlogSize
                    )
                }
            )
        }
    )

    public fun ApacheHttpAsyncClientProperties.toProperties(): io.github.zenhelix.web.http.client.apache.ApacheHttpAsyncClientProperties =
        io.github.zenhelix.web.http.client.apache.ApacheHttpAsyncClientProperties(
        connectionManagerShared = this.connectionManagerShared,
        defaultRequestConfig = RequestConfigProperties(
            connectionRequestTimeout = this.request?.connectionRequestTimeout,
            responseTimeout = this.readTimeout,
            connectionKeepAlive = this.request?.connectionKeepAlive,
            redirectsEnabled = this.request?.redirectsEnabled,
            circularRedirectsAllowed = this.request?.circularRedirectsAllowed,
            maxRedirects = this.request?.maxRedirects,
            protocolUpgradeEnabled = this.request?.protocolUpgradeEnabled
        ),

        useProxy = this.useProxy,
        proxy = this.proxy?.let {
            ProxyProperties(
                proxyHost = it.proxyHost, proxyPort = it.proxyPort,
                useSystem = it.useSystem
            )
        },
        poolingConnectionManager = this.pool?.let {
            PoolingAsyncConnectionManagerProperties(
                maxConnections = it.maxConnections,
                maxConnectionsPerRoute = it.maxConnectionsPerRoute,
                connectionConfig = ConnectionConfigProperties(
                    connectTimeout = this.connectTimeout,
                    connectionTimeToLive = it.connection?.connectionTimeToLive,
                    validateAfterInactivityTimeout = it.connection?.validateAfterInactivityTimeout,
                    socketTimeout = it.connection?.socketTimeout
                )
            )
        }
    )
}