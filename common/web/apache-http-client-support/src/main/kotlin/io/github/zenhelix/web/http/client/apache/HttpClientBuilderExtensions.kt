package io.github.zenhelix.web.http.client.apache

import org.apache.hc.client5.http.config.ConnectionConfig
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder
import org.apache.hc.core5.http.io.SocketConfig

public fun apacheHttpAsyncClient(
    builder: HttpAsyncClientBuilder = HttpAsyncClientBuilder.create(),
    initializer: HttpAsyncClientBuilder.() -> Unit = {}
): CloseableHttpAsyncClient = builder.apply(initializer).build()

public fun apacheHttpClient(
    builder: HttpClientBuilder = HttpClientBuilder.create(),
    initializer: HttpClientBuilder.() -> Unit = {}
): CloseableHttpClient = builder.apply(initializer).build()

public fun connectionConfig(
    builder: ConnectionConfig.Builder = ConnectionConfig.copy(ConnectionConfig.DEFAULT),
    initializer: ConnectionConfig.Builder.() -> Unit = {}
): ConnectionConfig = builder.apply(initializer).build()

public fun requestConfig(
    builder: RequestConfig.Builder = RequestConfig.copy(RequestConfig.DEFAULT),
    initializer: RequestConfig.Builder.() -> Unit = {}
): RequestConfig = builder.apply(initializer).build()

public fun socketConfig(
    builder: SocketConfig.Builder = SocketConfig.copy(SocketConfig.DEFAULT),
    initializer: SocketConfig.Builder.() -> Unit = {}
): SocketConfig = builder.apply(initializer).build()

public fun poolingHttpClientConnectionManager(
    builder: PoolingHttpClientConnectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create(),
    initializer: PoolingHttpClientConnectionManagerBuilder.() -> Unit = {}
): PoolingHttpClientConnectionManager = builder.apply(initializer).build()

public fun poolingAsyncClientConnectionManager(
    builder: PoolingAsyncClientConnectionManagerBuilder = PoolingAsyncClientConnectionManagerBuilder.create(),
    initializer: PoolingAsyncClientConnectionManagerBuilder.() -> Unit
): PoolingAsyncClientConnectionManager = builder.apply(initializer).build()
