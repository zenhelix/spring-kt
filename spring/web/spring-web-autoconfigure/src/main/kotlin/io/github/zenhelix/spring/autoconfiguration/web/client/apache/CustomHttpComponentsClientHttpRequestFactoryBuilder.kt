package io.github.zenhelix.spring.autoconfiguration.web.client.apache

import io.github.zenhelix.spring.autoconfiguration.web.client.CustomClientHttpRequestFactoryBuilder
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.core5.http.io.SocketConfig
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings
import org.springframework.boot.http.client.HttpComponentsClientHttpRequestFactoryBuilder
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import java.util.function.Consumer

/**
 * @see HttpComponentsClientHttpRequestFactoryBuilder
 */
public interface CustomHttpComponentsClientHttpRequestFactoryBuilder :
    CustomClientHttpRequestFactoryBuilder<HttpComponentsClientHttpRequestFactoryBuilder, HttpComponentsClientHttpRequestFactory, CustomHttpComponentsClientHttpRequestFactoryBuilder> {

    public fun httpClientCustomizer(customizer: Consumer<HttpClientBuilder>): CustomHttpComponentsClientHttpRequestFactoryBuilder
    public fun connectionManagerCustomizer(customizer: Consumer<PoolingHttpClientConnectionManagerBuilder>): CustomHttpComponentsClientHttpRequestFactoryBuilder
    public fun socketConfigCustomizer(customizer: Consumer<SocketConfig.Builder>): CustomHttpComponentsClientHttpRequestFactoryBuilder
    public fun requestConfigCustomizer(customizer: Consumer<RequestConfig.Builder>): CustomHttpComponentsClientHttpRequestFactoryBuilder

    public fun httpClientBuilderConfigurer(configurer: ApacheHttpClientBuilderConfigurer): CustomHttpComponentsClientHttpRequestFactoryBuilder
    public fun requestConfigBuilderConfigurer(configurer: ApacheRequestConfigBuilderConfigurer): CustomHttpComponentsClientHttpRequestFactoryBuilder
    public fun socketConfigBuilderConfigurer(configurer: ApacheSocketConfigBuilderConfigurer): CustomHttpComponentsClientHttpRequestFactoryBuilder
    public fun poolingHttpClientConnectionManagerBuilderConfigurer(configurer: ApachePoolingHttpClientConnectionManagerBuilderConfigurer): CustomHttpComponentsClientHttpRequestFactoryBuilder
    public fun clientHttpRequestFactoryConfigurer(configurer: HttpComponentsClientHttpRequestFactoryConfigurer): CustomHttpComponentsClientHttpRequestFactoryBuilder
}

public class DefaultCustomHttpComponentsClientHttpRequestFactoryBuilder : CustomHttpComponentsClientHttpRequestFactoryBuilder {
    private val httpClientCustomizers: MutableList<Consumer<HttpClientBuilder>> = mutableListOf()
    private val connectionManagerCustomizers: MutableList<Consumer<PoolingHttpClientConnectionManagerBuilder>> = mutableListOf()
    private val socketConfigCustomizers: MutableList<Consumer<SocketConfig.Builder>> = mutableListOf()
    private val requestConfigCustomizers: MutableList<Consumer<RequestConfig.Builder>> = mutableListOf()

    private val clientHttpRequestFactoryCustomizers: MutableList<Consumer<HttpComponentsClientHttpRequestFactory>> = mutableListOf()
    private var settings: ClientHttpRequestFactorySettings? = null

    private var httpClientBuilderConfigurer: ApacheHttpClientBuilderConfigurer? = null
    private var requestConfigBuilderConfigurer: ApacheRequestConfigBuilderConfigurer? = null
    private var socketConfigBuilderConfigurer: ApacheSocketConfigBuilderConfigurer? = null
    private var poolingHttpClientConnectionManagerBuilderConfigurer: ApachePoolingHttpClientConnectionManagerBuilderConfigurer? = null
    private var clientHttpRequestFactoryConfigurer: HttpComponentsClientHttpRequestFactoryConfigurer? = null

    override fun httpClientCustomizer(customizer: Consumer<HttpClientBuilder>): DefaultCustomHttpComponentsClientHttpRequestFactoryBuilder = apply {
        httpClientCustomizers.add(customizer)
    }

    override fun connectionManagerCustomizer(customizer: Consumer<PoolingHttpClientConnectionManagerBuilder>): DefaultCustomHttpComponentsClientHttpRequestFactoryBuilder =
        apply {
        connectionManagerCustomizers.add(customizer)
    }

    override fun socketConfigCustomizer(customizer: Consumer<SocketConfig.Builder>): DefaultCustomHttpComponentsClientHttpRequestFactoryBuilder = apply {
        socketConfigCustomizers.add(customizer)
    }

    override fun requestConfigCustomizer(customizer: Consumer<RequestConfig.Builder>): DefaultCustomHttpComponentsClientHttpRequestFactoryBuilder = apply {
        requestConfigCustomizers.add(customizer)
    }

    override fun httpClientBuilderConfigurer(configurer: ApacheHttpClientBuilderConfigurer): DefaultCustomHttpComponentsClientHttpRequestFactoryBuilder =
        apply {
        this.httpClientBuilderConfigurer = configurer
    }

    override fun requestConfigBuilderConfigurer(configurer: ApacheRequestConfigBuilderConfigurer): DefaultCustomHttpComponentsClientHttpRequestFactoryBuilder =
        apply {
        this.requestConfigBuilderConfigurer = configurer
    }

    override fun socketConfigBuilderConfigurer(configurer: ApacheSocketConfigBuilderConfigurer): DefaultCustomHttpComponentsClientHttpRequestFactoryBuilder =
        apply {
        this.socketConfigBuilderConfigurer = configurer
    }

    override fun poolingHttpClientConnectionManagerBuilderConfigurer(configurer: ApachePoolingHttpClientConnectionManagerBuilderConfigurer): DefaultCustomHttpComponentsClientHttpRequestFactoryBuilder =
        apply {
        this.poolingHttpClientConnectionManagerBuilderConfigurer = configurer
    }

    override fun clientHttpRequestFactoryConfigurer(configurer: HttpComponentsClientHttpRequestFactoryConfigurer): DefaultCustomHttpComponentsClientHttpRequestFactoryBuilder =
        apply {
        this.clientHttpRequestFactoryConfigurer = configurer
    }

    override fun customizer(customizer: Consumer<HttpComponentsClientHttpRequestFactory>): DefaultCustomHttpComponentsClientHttpRequestFactoryBuilder = apply {
        clientHttpRequestFactoryCustomizers.add(customizer)
    }

    override fun settings(settings: ClientHttpRequestFactorySettings): DefaultCustomHttpComponentsClientHttpRequestFactoryBuilder = apply {
        this.settings = settings
    }

    override fun applyOther(builderConsumer: Consumer<CustomHttpComponentsClientHttpRequestFactoryBuilder>): DefaultCustomHttpComponentsClientHttpRequestFactoryBuilder =
        apply {
        builderConsumer.accept(this)
    }

    override fun build(builder: HttpComponentsClientHttpRequestFactoryBuilder): HttpComponentsClientHttpRequestFactory = builder
        .let {
            httpClientBuilderConfigurer?.accumulate()?.also { c -> httpClientCustomizers.add(0, Consumer { c.customize(it) }) }
            if (httpClientCustomizers.isNotEmpty()) {
                it.withHttpClientCustomizer(httpClientCustomizers.reduce { acc, consumer -> acc.andThen(consumer) })
            } else {
                it
            }
        }
        .let {
            socketConfigBuilderConfigurer?.accumulate()?.also { c -> socketConfigCustomizers.add(0, Consumer { c.customize(it) }) }
            if (socketConfigCustomizers.isNotEmpty()) {
                it.withSocketConfigCustomizer(socketConfigCustomizers.reduce { acc, consumer -> acc.andThen(consumer) })
            } else {
                it
            }
        }
        .let {
            requestConfigBuilderConfigurer?.accumulate()?.also { c -> requestConfigCustomizers.add(0, Consumer { c.customize(it) }) }
            if (requestConfigCustomizers.isNotEmpty()) {
                it.withDefaultRequestConfigCustomizer(requestConfigCustomizers.reduce { acc, consumer -> acc.andThen(consumer) })
            } else {
                it
            }
        }
        .let {
            poolingHttpClientConnectionManagerBuilderConfigurer?.accumulate()?.also { c -> connectionManagerCustomizers.add(0, Consumer { c.customize(it) }) }
            if (connectionManagerCustomizers.isNotEmpty()) {
                it.withConnectionManagerCustomizer(connectionManagerCustomizers.reduce { acc, consumer -> acc.andThen(consumer) })
            } else {
                it
            }
        }
        .let {
            clientHttpRequestFactoryConfigurer?.accumulate()?.also { c -> clientHttpRequestFactoryCustomizers.add(0, c) }
            if (clientHttpRequestFactoryCustomizers.isNotEmpty()) {
                it.withCustomizers(clientHttpRequestFactoryCustomizers)
            } else {
                it
            }
        }
        .let {
            if (settings != null) {
                it.build(settings)
            } else {
                it.build()
            }
        }
}
