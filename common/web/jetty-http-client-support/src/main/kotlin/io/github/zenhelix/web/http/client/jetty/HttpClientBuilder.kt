package io.github.zenhelix.web.http.client.jetty

import org.eclipse.jetty.client.HttpClient
import org.eclipse.jetty.client.HttpProxy
import org.eclipse.jetty.client.transport.HttpClientTransportOverHTTP
import org.eclipse.jetty.io.ByteBufferPool
import org.eclipse.jetty.io.ClientConnector
import org.eclipse.jetty.util.ProcessorUtils
import org.eclipse.jetty.util.ssl.SslContextFactory
import org.eclipse.jetty.util.thread.Scheduler
import java.time.Duration
import java.util.concurrent.Executor
import javax.net.ssl.SSLContext
import kotlin.math.max

public class HttpClientBuilder {

    private var connectTimeout: Duration? = null
    private var idleTimeout: Duration? = null
    private var ignoreSsl: Boolean? = null
    private var proxy: ProxyProperties? = null

    private var executor: Executor? = null
    private var byteBufferPool: ByteBufferPool? = null
    private var scheduler: Scheduler? = null

    private var sslContext: SSLContext? = null

    private var builderConsumer: ((HttpClient) -> HttpClient) = { it }

    public fun connectTimeout(connectTimeout: Duration): HttpClientBuilder = apply { this.connectTimeout = connectTimeout }
    public fun idleTimeout(idleTimeout: Duration): HttpClientBuilder = apply { this.idleTimeout = idleTimeout }
    public fun ignoreSsl(ignoreSsl: Boolean): HttpClientBuilder = apply { this.ignoreSsl = ignoreSsl }
    public fun proxy(proxy: ProxyProperties): HttpClientBuilder = apply { this.proxy = proxy }

    public fun executor(executor: Executor): HttpClientBuilder = apply { this.executor = executor }
    public fun byteBufferPool(pool: ByteBufferPool): HttpClientBuilder = apply { this.byteBufferPool = pool }
    public fun scheduler(scheduler: Scheduler): HttpClientBuilder = apply { this.scheduler = scheduler }

    public fun sslContext(sslContext: SSLContext): HttpClientBuilder = apply { this.sslContext = sslContext }

    public fun builderConsumer(consumer: (HttpClient) -> HttpClient): HttpClientBuilder = apply { this.builderConsumer = consumer }

    public fun build(): HttpClient = HttpClient(HttpClientTransportOverHTTP(ClientConnector().apply {
        this.selectors = max(1, ProcessorUtils.availableProcessors() / 2)

        this@HttpClientBuilder.connectTimeout?.also { this.connectTimeout = it }
        this@HttpClientBuilder.idleTimeout?.also { this.idleTimeout = it }

        if (sslContext != null) {
            this.sslContextFactory = SslContextFactory.Client().apply {
                this.sslContext = this@HttpClientBuilder.sslContext
            }
        } else {
            this@HttpClientBuilder.ignoreSsl?.also {
                this.sslContextFactory = SslContextFactory.Client(true).apply {
                    endpointIdentificationAlgorithm = "HTTPS"
                }
            }
        }

        this@HttpClientBuilder.executor?.also { this.executor = it }
        this@HttpClientBuilder.byteBufferPool?.also { this.byteBufferPool = it }
        this@HttpClientBuilder.scheduler?.also { this.scheduler = it }

    })).apply {
        proxy?.also {
            this.proxyConfiguration.addProxy(HttpProxy(it.proxyHost, it.proxyPort ?: 8080))
        }
    }.let { builderConsumer(it) }

}