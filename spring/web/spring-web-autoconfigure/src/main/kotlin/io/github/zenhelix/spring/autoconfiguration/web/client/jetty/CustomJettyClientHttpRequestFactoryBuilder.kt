package io.github.zenhelix.spring.autoconfiguration.web.client.jetty

import io.github.zenhelix.spring.autoconfiguration.web.client.CustomClientHttpRequestFactoryBuilder
import org.eclipse.jetty.client.HttpClient
import org.eclipse.jetty.client.HttpClientTransport
import org.eclipse.jetty.io.ClientConnector
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings
import org.springframework.boot.http.client.JettyClientHttpRequestFactoryBuilder
import org.springframework.http.client.JettyClientHttpRequestFactory
import java.util.function.Consumer

public interface CustomJettyClientHttpRequestFactoryBuilder :
    CustomClientHttpRequestFactoryBuilder<JettyClientHttpRequestFactoryBuilder, JettyClientHttpRequestFactory, CustomJettyClientHttpRequestFactoryBuilder> {

    public fun httpClientCustomizer(customizer: Consumer<HttpClient>): CustomJettyClientHttpRequestFactoryBuilder
    public fun httpClientBuilderConfigurer(configurer: JettyHttpClientConfigurer): CustomJettyClientHttpRequestFactoryBuilder

    public fun httpClientTransportCustomizer(customizer: Consumer<HttpClientTransport>): CustomJettyClientHttpRequestFactoryBuilder
    public fun httpClientTransportConfigurer(configurer: JettyHttpClientTransportConfigurer): CustomJettyClientHttpRequestFactoryBuilder

    public fun clientConnectorCustomizer(customizer: Consumer<ClientConnector>): CustomJettyClientHttpRequestFactoryBuilder
    public fun clientConnectorConfigurer(configurer: JettyClientConnectorConfigurer): CustomJettyClientHttpRequestFactoryBuilder

    public fun clientHttpRequestFactoryConfigurer(configurer: JettyClientHttpRequestFactoryConfigurer): CustomJettyClientHttpRequestFactoryBuilder
}

public class DefaultCustomJettyClientHttpRequestFactoryBuilder : CustomJettyClientHttpRequestFactoryBuilder {
    private val httpClientCustomizers: MutableList<Consumer<HttpClient>> = mutableListOf()
    private var httpClientConfigurer: JettyHttpClientConfigurer? = null

    private val httpClientTransportCustomizers: MutableList<Consumer<HttpClientTransport>> = mutableListOf()
    private var httpClientTransportConfigurer: JettyHttpClientTransportConfigurer? = null

    private val clientConnectorCustomizers: MutableList<Consumer<ClientConnector>> = mutableListOf()
    private var clientConnectorConfigurer: JettyClientConnectorConfigurer? = null

    private val clientHttpRequestFactoryCustomizers: MutableList<Consumer<JettyClientHttpRequestFactory>> = mutableListOf()
    private var clientHttpRequestFactoryConfigurer: JettyClientHttpRequestFactoryConfigurer? = null

    private var settings: ClientHttpRequestFactorySettings? = null

    override fun httpClientCustomizer(customizer: Consumer<HttpClient>): DefaultCustomJettyClientHttpRequestFactoryBuilder = apply {
        httpClientCustomizers.add(customizer)
    }

    override fun httpClientBuilderConfigurer(configurer: JettyHttpClientConfigurer): DefaultCustomJettyClientHttpRequestFactoryBuilder = apply {
        this.httpClientConfigurer = configurer
    }

    override fun httpClientTransportCustomizer(customizer: Consumer<HttpClientTransport>): DefaultCustomJettyClientHttpRequestFactoryBuilder = apply {
        httpClientTransportCustomizers.add(customizer)
    }

    override fun httpClientTransportConfigurer(configurer: JettyHttpClientTransportConfigurer): DefaultCustomJettyClientHttpRequestFactoryBuilder = apply {
        this.httpClientTransportConfigurer = configurer
    }

    override fun clientConnectorCustomizer(customizer: Consumer<ClientConnector>): DefaultCustomJettyClientHttpRequestFactoryBuilder = apply {
        clientConnectorCustomizers.add(customizer)
    }

    override fun clientConnectorConfigurer(configurer: JettyClientConnectorConfigurer): DefaultCustomJettyClientHttpRequestFactoryBuilder = apply {
        this.clientConnectorConfigurer = configurer
    }

    override fun clientHttpRequestFactoryConfigurer(configurer: JettyClientHttpRequestFactoryConfigurer): DefaultCustomJettyClientHttpRequestFactoryBuilder =
        apply {
        this.clientHttpRequestFactoryConfigurer = configurer
    }

    override fun customizer(customizer: Consumer<JettyClientHttpRequestFactory>): DefaultCustomJettyClientHttpRequestFactoryBuilder = apply {
        clientHttpRequestFactoryCustomizers.add(customizer)
    }

    override fun settings(settings: ClientHttpRequestFactorySettings): DefaultCustomJettyClientHttpRequestFactoryBuilder = apply {
        this.settings = settings
    }

    override fun applyOther(builderConsumer: Consumer<CustomJettyClientHttpRequestFactoryBuilder>): DefaultCustomJettyClientHttpRequestFactoryBuilder = apply {
        builderConsumer.accept(this)
    }

    override fun build(builder: JettyClientHttpRequestFactoryBuilder): JettyClientHttpRequestFactory = builder
        .let {
            httpClientConfigurer?.accumulate()?.also { c -> httpClientCustomizers.add(0, c) }
            if (httpClientCustomizers.isNotEmpty()) {
                it.withHttpClientCustomizer(httpClientCustomizers.reduce { acc, consumer -> acc.andThen(consumer) })
            } else {
                it
            }
        }
        .let {
            httpClientTransportConfigurer?.accumulate()?.also { c -> httpClientTransportCustomizers.add(0, c) }
            if (httpClientTransportCustomizers.isNotEmpty()) {
                it.withHttpClientTransportCustomizer(httpClientTransportCustomizers.reduce { acc, consumer -> acc.andThen(consumer) })
            } else {
                it
            }
        }
        .let {
            clientConnectorConfigurer?.accumulate()?.also { c -> clientConnectorCustomizers.add(0, c) }
            if (clientConnectorCustomizers.isNotEmpty()) {
                it.withClientConnectorCustomizerCustomizer(clientConnectorCustomizers.reduce { acc, consumer -> acc.andThen(consumer) })
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