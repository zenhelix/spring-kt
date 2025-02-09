package io.github.zenhelix.web.http.client.netty.reactor

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.handler.ssl.SslContext
import io.netty.handler.timeout.WriteTimeoutHandler
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import reactor.netty.resources.LoopResources
import reactor.netty.transport.ProxyProvider
import java.time.Duration
import java.util.LinkedList
import java.util.concurrent.TimeUnit

public class HttpClientBuilder {

    private var connectionProvider: ConnectionProvider? = null

    private var channelResources: LoopResources? = null
    private var preferNative: Boolean? = null
    private var eventLoopGroup: EventLoopGroup? = null

    private var baseUrl: String? = null

    private var compress: Boolean? = null
    private var disableRetry: Boolean? = null
    private var followRedirect: Boolean? = null
    private var keepAlive: Boolean? = null

    private var connectTimeout: Duration? = null
    private var readTimeout: Duration? = null
    private var writeTimeout: Duration? = null

    private val channelHandlers: LinkedList<ChannelHandler> = LinkedList<ChannelHandler>()

    private var proxy: ProxyProperties? = null

    private var ignoreSsl: Boolean? = null
    private var sslContext: SslContext? = null

    private var builderConsumer: ((HttpClient) -> HttpClient) = { it }

    public fun connectionProvider(connectionProvider: ConnectionProvider): HttpClientBuilder = apply { this.connectionProvider = connectionProvider }

    public fun channelResources(channelResources: LoopResources): HttpClientBuilder = apply { this.channelResources = channelResources }
    public fun channelResources(channelResources: LoopResources, preferNative: Boolean): HttpClientBuilder = apply {
        this.channelResources = channelResources
        this.preferNative = preferNative
    }

    public fun eventLoopGroup(eventLoopGroup: EventLoopGroup): HttpClientBuilder = apply { this.eventLoopGroup = eventLoopGroup }

    public fun baseUrl(baseUrl: String): HttpClientBuilder = apply { this.baseUrl = baseUrl }

    public fun keepAlive(keepAlive: Boolean): HttpClientBuilder = apply { this.keepAlive = keepAlive }
    public fun compress(compress: Boolean): HttpClientBuilder = apply { this.compress = compress }
    public fun disableRetry(disableRetry: Boolean): HttpClientBuilder = apply { this.disableRetry = disableRetry }
    public fun followRedirect(followRedirect: Boolean): HttpClientBuilder = apply { this.followRedirect = followRedirect }

    public fun connectTimeout(connectTimeout: Duration): HttpClientBuilder = apply { this.connectTimeout = connectTimeout }
    public fun readTimeout(readTimeout: Duration): HttpClientBuilder = apply { this.readTimeout = readTimeout }
    public fun writeTimeout(writeTimeout: Duration): HttpClientBuilder = apply { this.writeTimeout = writeTimeout }

    public fun channelHandler(vararg channelHandler: ChannelHandler): HttpClientBuilder = apply {
        channelHandlers(channelHandler.toList())
    }

    public fun channelHandlers(channelHandlers: Collection<ChannelHandler>): HttpClientBuilder = apply {
        this.channelHandlers.addAll(channelHandlers)
    }

    public fun proxy(proxy: ProxyProperties): HttpClientBuilder = apply { this.proxy = proxy }

    public fun ignoreSsl(ignoreSsl: Boolean): HttpClientBuilder = apply { this.ignoreSsl = ignoreSsl }
    public fun sslContext(sslContext: SslContext): HttpClientBuilder = apply { this.sslContext = sslContext }

    public fun builderConsumer(consumer: (HttpClient) -> HttpClient): HttpClientBuilder = apply { this.builderConsumer = consumer }

    public fun build(initConnectionProvider: ConnectionProvider? = null): HttpClient {
        if (channelResources != null && eventLoopGroup != null) {
            throw IllegalStateException("Channel resource already set")
        }

        if (connectionProvider == null && initConnectionProvider != null) {
            connectionProvider = initConnectionProvider
        }

        val httpClient = connectionProvider?.let { HttpClient.create(it) } ?: HttpClient.create()

        if (channelResources != null && preferNative != null) {
            httpClient.runOn(channelResources!!, preferNative!!)
        } else if (channelResources != null) {
            channelResources?.also { httpClient.runOn(it) }
        }

        eventLoopGroup?.also { httpClient.runOn(it) }
        baseUrl?.also { httpClient.baseUrl(it) }
        compress?.also { httpClient.compress(it) }
        disableRetry?.also { httpClient.disableRetry(it) }
        followRedirect?.also { httpClient.followRedirect(it) }
        keepAlive?.also { httpClient.keepAlive(it) }

        connectTimeout?.also { channelHandlers.add(ReadTimeoutHandler(it.toMillis(), TimeUnit.MILLISECONDS)) }
        writeTimeout?.also { channelHandlers.add(WriteTimeoutHandler(it.toMillis(), TimeUnit.MILLISECONDS)) }
        connectTimeout?.also { httpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, it.toMillis().toInt()) }

        channelHandlers.takeIf { it.isNotEmpty() }?.also {
            httpClient.doOnConnected { connection -> it.forEach { connection.addHandlerLast(it) } }
        }

        proxy?.also { httpClient.httpProxy(it.proxyHost, it.proxyPort, it.useSystem) }

        if (ignoreSsl == true) {
            httpClient.secure { it.insecureSslContext() }
        } else {
            sslContext?.also { sslContext ->
                httpClient.secure { it.sslContext(sslContext) }
            }
        }

        return builderConsumer(httpClient)
    }

    private fun HttpClient.httpProxy(proxyHost: String?, proxyPort: Int?, useSystem: Boolean = true): HttpClient {
        val thisProxyHost: String
        val thisProxyPort: Int?

        val systemProxyHost = System.getProperty("https.proxyHost")
        val systemProxyPort = System.getProperty("https.proxyPort")

        if (useSystem && systemProxyHost != null && systemProxyPort != null) {
            thisProxyHost = systemProxyHost
            thisProxyPort = systemProxyPort.toInt()
        } else {
            thisProxyHost = proxyHost ?: throw IllegalStateException()
            thisProxyPort = proxyPort
        }

        return this.proxy {
            it
                .type(ProxyProvider.Proxy.HTTP)
                .host(thisProxyHost)
                .apply {
                    if (thisProxyPort != null) {
                        this.port(thisProxyPort)
                    }
                }
        }

    }

}