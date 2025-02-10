package io.github.zenhelix.web.http.client.jdk

import java.net.http.HttpClient
import java.time.Duration

public data class JdkHttpClientProperties(

    val connectTimeout: Duration?,

    val redirectType: HttpClient.Redirect?,

    val useProxy: Boolean = false,
    val proxy: ProxyProperties? = null
)

public data class ProxyProperties(
    val proxyHost: String,
    val proxyPort: Int?,
    val useSystem: Boolean = true
)