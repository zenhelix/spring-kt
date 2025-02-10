package io.github.zenhelix.web.http.client.apache

import io.github.zenhelix.web.http.client.apache.ApacheHttpClientFactory.connectionConfig
import org.apache.hc.client5.http.config.ConnectionConfig
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder
import org.apache.hc.core5.http.HttpHost

public object ApacheHttpAsyncClientFactory {

    public fun apacheHttpAsyncClient(
        properties: ApacheHttpAsyncClientProperties,
        requestConfigInitializer: RequestConfig.Builder.() -> Unit = {},
        connectionConfigInitializer: (ConnectionConfig.Builder.() -> Unit) = {},
        poolingHttpClientConnectionManagerInitializer: PoolingAsyncClientConnectionManagerBuilder.() -> Unit = {},
        builder: HttpAsyncClientBuilder = HttpAsyncClientBuilder.create(),
        initializer: HttpAsyncClientBuilder.() -> Unit = {}
    ): CloseableHttpAsyncClient = apacheHttpAsyncClient(builder) {
        apacheHttpAsyncClient(
            properties,
            requestConfigInitializer, connectionConfigInitializer,
            poolingHttpClientConnectionManagerInitializer,
            initializer
        )
    }

    public fun HttpAsyncClientBuilder.apacheHttpAsyncClient(
        properties: ApacheHttpAsyncClientProperties,
        requestConfigInitializer: RequestConfig.Builder.() -> Unit = {},
        connectionConfigInitializer: (ConnectionConfig.Builder.() -> Unit) = {},
        poolingHttpClientConnectionManagerInitializer: PoolingAsyncClientConnectionManagerBuilder.() -> Unit = {},
        initializer: HttpAsyncClientBuilder.() -> Unit = {}
    ): HttpAsyncClientBuilder = apply {
        properties.defaultRequestConfig?.also { setDefaultRequestConfig(ApacheHttpClientFactory.requestConfig(it, initializer = requestConfigInitializer)) }
        properties.connectionManagerShared?.also { setConnectionManagerShared(it) }
        properties.poolingConnectionManager?.also {
            useSystemProperties().setConnectionManager(
                poolingAsyncClientConnectionManager(
                    it,
                    connectionConfigInitializer,
                    initializer = poolingHttpClientConnectionManagerInitializer
                )
            )
        }

        if (properties.useProxy && properties.proxy != null) {
            setProxy(HttpHost(properties.proxy.proxyHost, properties.proxy.proxyPort ?: -1))
        }

        apply(initializer)
    }

    public fun poolingAsyncClientConnectionManager(
        properties: PoolingAsyncConnectionManagerProperties,
        connectionConfigInitializer: (ConnectionConfig.Builder.() -> Unit) = {},
        builder: PoolingAsyncClientConnectionManagerBuilder = PoolingAsyncClientConnectionManagerBuilder.create(),
        initializer: PoolingAsyncClientConnectionManagerBuilder.() -> Unit = {}
    ): PoolingAsyncClientConnectionManager = poolingAsyncClientConnectionManager(builder) {
        poolingAsyncClientConnectionManager(properties, connectionConfigInitializer, initializer)
    }

    public fun PoolingAsyncClientConnectionManagerBuilder.poolingAsyncClientConnectionManager(
        properties: PoolingAsyncConnectionManagerProperties,
        connectionConfigInitializer: (ConnectionConfig.Builder.() -> Unit) = { },
        initializer: PoolingAsyncClientConnectionManagerBuilder.() -> Unit = {}
    ): PoolingAsyncClientConnectionManagerBuilder = apply {
        properties.maxConnections?.also { setMaxConnTotal(it) }
        properties.maxConnectionsPerRoute?.also { setMaxConnPerRoute(it) }

        properties.connectionConfig?.let { connectionConfig(it, initializer = connectionConfigInitializer) }?.also { setDefaultConnectionConfig(it) }

        this.apply(initializer)
    }

}
