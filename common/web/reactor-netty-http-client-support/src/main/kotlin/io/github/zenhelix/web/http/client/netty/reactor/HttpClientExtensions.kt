package io.github.zenhelix.web.http.client.netty.reactor

import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import reactor.netty.tcp.SslProvider

public fun nettyReactorHttpClient(
    connectionProvider: ConnectionProvider? = null, initializer: HttpClientBuilder.() -> Unit = {}
): HttpClient = HttpClientBuilder().apply(initializer).build(connectionProvider)

public fun sslContextForClient(initializer: SslContextBuilder.() -> Unit = {}): SslContext = SslContextBuilder.forClient().apply(initializer).build()

public fun sslProvider(
    initializer: SslProvider.SslContextSpec.() -> Unit = {}
): SslProvider = (SslProvider.builder().apply(initializer) as SslProvider.Builder).build()

public fun SslProvider.SslContextSpec.insecureSslContext(): SslProvider.Builder = this.sslContext(
    sslContextForClient { trustManager(InsecureTrustManagerFactory.INSTANCE) }
)
