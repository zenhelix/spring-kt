package io.github.zenhelix.web.http.client.netty.reactor

import java.time.Duration

public data class NettyReactorHttpClientProperties(

    val baseUrl: String? = null,

    val compress: Boolean? = null,
    val disableRetry: Boolean? = null,
    val followRedirect: Boolean? = null,
    val keepAlive: Boolean? = null,

    val connectTimeout: Duration? = null,
    val readTimeout: Duration? = null,
    val writeTimeout: Duration? = null,

    val ignoreSsl: Boolean? = null,

    val useProxy: Boolean = false,
    val proxy: ProxyProperties? = null
)

public data class ProxyProperties(
    val proxyHost: String,
    val proxyPort: Int?,
    val useSystem: Boolean = true
)