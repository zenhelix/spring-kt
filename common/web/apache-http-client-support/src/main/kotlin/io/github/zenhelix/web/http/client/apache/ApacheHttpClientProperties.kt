package io.github.zenhelix.web.http.client.apache

import java.time.Duration

public data class ApacheHttpClientProperties(

    val connectionManagerShared: Boolean? = null,

    val defaultRequestConfig: RequestConfigProperties? = null,

    val useProxy: Boolean = false,
    val proxy: ProxyProperties? = null,

    val poolingConnectionManager: PoolingConnectionManagerProperties? = null
)

public data class ProxyProperties(
    val proxyHost: String,
    val proxyPort: Int? = null,
    val useSystem: Boolean = true
)

public data class PoolingConnectionManagerProperties(
    val maxConnections: Int? = null,
    val maxConnectionsPerRoute: Int? = null,
    val connectionConfig: ConnectionConfigProperties? = null,
    val socketConfig: SocketConfigProperties? = null
)

public data class RequestConfigProperties(
    val connectionRequestTimeout: Duration? = null,
    val responseTimeout: Duration? = null,
    val connectionKeepAlive: Duration? = null,

    val redirectsEnabled: Boolean? = null,
    val circularRedirectsAllowed: Boolean? = null,
    val maxRedirects: Int? = null,

    val protocolUpgradeEnabled: Boolean? = null
)

public data class ConnectionConfigProperties(
    /** @see org.apache.hc.client5.http.config.ConnectionConfig.DEFAULT_CONNECT_TIMEOUT */
    val connectTimeout: Duration? = null,
    val connectionTimeToLive: Duration? = null,
    val validateAfterInactivityTimeout: Duration? = null,
    val socketTimeout: Duration? = null
)

public data class SocketConfigProperties(
    val soTimeout: Duration? = null,
    val soReuseAddress: Boolean? = null,
    val soKeepAlive: Boolean? = null,
    val tcpNoDelay: Boolean? = null,
    val sndBufSize: Int? = null,
    val rcvBufSize: Int? = null,
    val backlogSize: Int? = null
)