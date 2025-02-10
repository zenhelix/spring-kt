package io.github.zenhelix.web.http.client.apache

public data class ApacheHttpAsyncClientProperties(
    val connectionManagerShared: Boolean? = null,

    val defaultRequestConfig: RequestConfigProperties? = null,

    val useProxy: Boolean = false,
    val proxy: ProxyProperties? = null,

    val poolingConnectionManager: PoolingAsyncConnectionManagerProperties? = null
)

public data class PoolingAsyncConnectionManagerProperties(
    val maxConnections: Int? = null,
    val maxConnectionsPerRoute: Int? = null,
    val connectionConfig: ConnectionConfigProperties? = null,
)