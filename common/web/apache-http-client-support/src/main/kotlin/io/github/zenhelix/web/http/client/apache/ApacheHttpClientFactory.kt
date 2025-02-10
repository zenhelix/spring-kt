package io.github.zenhelix.web.http.client.apache

import org.apache.hc.client5.http.config.ConnectionConfig
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.core5.http.HttpHost
import org.apache.hc.core5.http.io.SocketConfig
import org.apache.hc.core5.util.Timeout

public object ApacheHttpClientFactory {

    public fun apacheHttpClient(
        properties: ApacheHttpClientProperties,
        requestConfigInitializer: RequestConfig.Builder.() -> Unit = {},
        connectionConfigInitializer: (ConnectionConfig.Builder.() -> Unit) = {},
        socketConfigInitializer: (SocketConfig.Builder.() -> Unit) = {},
        poolingHttpClientConnectionManagerInitializer: PoolingHttpClientConnectionManagerBuilder.() -> Unit = {},
        builder: HttpClientBuilder = HttpClientBuilder.create(),
        initializer: HttpClientBuilder.() -> Unit = {}
    ): CloseableHttpClient = apacheHttpClient(builder) {
        apacheHttpClient(
            properties,
            requestConfigInitializer, connectionConfigInitializer, socketConfigInitializer,
            poolingHttpClientConnectionManagerInitializer,
            initializer
        )
    }

    public fun HttpClientBuilder.apacheHttpClient(
        properties: ApacheHttpClientProperties,
        requestConfigInitializer: RequestConfig.Builder.() -> Unit = {},
        connectionConfigInitializer: (ConnectionConfig.Builder.() -> Unit) = {},
        socketConfigInitializer: (SocketConfig.Builder.() -> Unit) = {},
        poolingHttpClientConnectionManagerInitializer: PoolingHttpClientConnectionManagerBuilder.() -> Unit = {},
        initializer: HttpClientBuilder.() -> Unit = {}
    ): HttpClientBuilder = apply {
        properties.defaultRequestConfig?.also { setDefaultRequestConfig(requestConfig(it, initializer = requestConfigInitializer)) }
        properties.connectionManagerShared?.also { setConnectionManagerShared(it) }
        properties.poolingConnectionManager?.also {
            useSystemProperties().setConnectionManager(
                poolingHttpClientConnectionManager(
                    it,
                    connectionConfigInitializer, socketConfigInitializer,
                    initializer = poolingHttpClientConnectionManagerInitializer
                )
            )
        }

        if (properties.useProxy && properties.proxy != null) {
            setProxy(HttpHost(properties.proxy.proxyHost, properties.proxy.proxyPort ?: -1))
        }

        apply(initializer)
    }

    public fun socketConfig(
        properties: SocketConfigProperties,
        builder: SocketConfig.Builder = SocketConfig.copy(SocketConfig.DEFAULT),
        initializer: SocketConfig.Builder.() -> Unit = {}
    ): SocketConfig = socketConfig(builder) { socketConfig(properties, initializer) }

    public fun SocketConfig.Builder.socketConfig(
        properties: SocketConfigProperties, initializer: SocketConfig.Builder.() -> Unit = {}
    ): SocketConfig.Builder = apply {
        properties.soTimeout?.let { Timeout.of(it) }?.also { setSoTimeout(it) }
        properties.soReuseAddress?.also { setSoReuseAddress(it) }
        properties.soKeepAlive?.also { setSoKeepAlive(it) }
        properties.tcpNoDelay?.also { setTcpNoDelay(it) }
        properties.sndBufSize?.also { setSndBufSize(it) }
        properties.rcvBufSize?.also { setRcvBufSize(it) }
        properties.backlogSize?.also { setBacklogSize(it) }

        this.apply(initializer)
    }

    public fun connectionConfig(
        properties: ConnectionConfigProperties,
        builder: ConnectionConfig.Builder = ConnectionConfig.copy(ConnectionConfig.DEFAULT),
        initializer: ConnectionConfig.Builder.() -> Unit = {}
    ): ConnectionConfig = connectionConfig(builder) { connectionConfig(properties, initializer) }

    public fun ConnectionConfig.Builder.connectionConfig(
        properties: ConnectionConfigProperties, initializer: ConnectionConfig.Builder.() -> Unit = {}
    ): ConnectionConfig.Builder = apply {
        properties.connectTimeout?.let { Timeout.of(it) }?.also { setConnectTimeout(it) }
        properties.connectionTimeToLive?.let { Timeout.of(it) }?.also { setTimeToLive(it) }
        properties.validateAfterInactivityTimeout?.let { Timeout.of(it) }?.also { setValidateAfterInactivity(it) }
        properties.socketTimeout?.let { Timeout.of(it) }?.also { setSocketTimeout(it) }

        this.apply(initializer)
    }

    public fun requestConfig(
        properties: RequestConfigProperties,
        builder: RequestConfig.Builder = RequestConfig.copy(RequestConfig.DEFAULT),
        initializer: RequestConfig.Builder.() -> Unit = {}
    ): RequestConfig = requestConfig(builder) { requestConfig(properties, initializer) }

    public fun RequestConfig.Builder.requestConfig(
        properties: RequestConfigProperties, initializer: RequestConfig.Builder.() -> Unit = {}
    ): RequestConfig.Builder = apply {
        properties.responseTimeout?.let { Timeout.of(it) }.also { setResponseTimeout(it) }
        properties.connectionRequestTimeout?.let { Timeout.of(it) }.also { setConnectionRequestTimeout(it) }
        properties.connectionKeepAlive?.let { Timeout.of(it) }.also { setConnectionKeepAlive(it) }

        properties.redirectsEnabled?.also { setRedirectsEnabled(it) }
        properties.circularRedirectsAllowed?.also { setCircularRedirectsAllowed(it) }
        properties.maxRedirects?.also { setMaxRedirects(it) }

        properties.protocolUpgradeEnabled?.also { setProtocolUpgradeEnabled(it) }

        this.apply(initializer)
    }

    public fun poolingHttpClientConnectionManager(
        properties: PoolingConnectionManagerProperties,
        connectionConfigInitializer: (ConnectionConfig.Builder.() -> Unit) = {},
        socketConfigInitializer: (SocketConfig.Builder.() -> Unit) = {},
        builder: PoolingHttpClientConnectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create(),
        initializer: PoolingHttpClientConnectionManagerBuilder.() -> Unit = {}
    ): PoolingHttpClientConnectionManager = poolingHttpClientConnectionManager(builder) {
        poolingHttpClientConnectionManager(properties, connectionConfigInitializer, socketConfigInitializer, initializer)
    }

    public fun PoolingHttpClientConnectionManagerBuilder.poolingHttpClientConnectionManager(
        properties: PoolingConnectionManagerProperties,
        connectionConfigInitializer: (ConnectionConfig.Builder.() -> Unit) = { },
        socketConfigInitializer: (SocketConfig.Builder.() -> Unit) = { },
        initializer: PoolingHttpClientConnectionManagerBuilder.() -> Unit = {}
    ): PoolingHttpClientConnectionManagerBuilder = apply {
        properties.maxConnections?.also { setMaxConnTotal(it) }
        properties.maxConnectionsPerRoute?.also { setMaxConnPerRoute(it) }

        properties.connectionConfig?.let { connectionConfig(it, initializer = connectionConfigInitializer) }?.also { setDefaultConnectionConfig(it) }
        properties.socketConfig?.let { socketConfig(it, initializer = socketConfigInitializer) }?.also { setDefaultSocketConfig(it) }

        this.apply(initializer)
    }

}
