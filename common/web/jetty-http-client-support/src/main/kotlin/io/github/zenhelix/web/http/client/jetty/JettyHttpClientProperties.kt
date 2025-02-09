package io.github.zenhelix.web.http.client.jetty

import java.time.Duration

public data class JettyHttpClientProperties(

    val connectTimeout: Duration? = null,
    val idleTimeout: Duration? = null,

    val ignoreSsl: Boolean? = null,

    val useProxy: Boolean = false,
    val proxy: ProxyProperties? = null
)

public data class ProxyProperties(
    val proxyHost: String,
    val proxyPort: Int?,
    val useSystem: Boolean = true
)