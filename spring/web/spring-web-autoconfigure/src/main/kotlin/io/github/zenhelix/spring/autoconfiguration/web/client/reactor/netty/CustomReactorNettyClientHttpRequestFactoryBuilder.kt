package io.github.zenhelix.spring.autoconfiguration.web.client.reactor.netty

import io.github.zenhelix.spring.autoconfiguration.web.client.CustomClientHttpRequestFactoryBuilder
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings
import org.springframework.boot.http.client.ReactorClientHttpRequestFactoryBuilder
import org.springframework.http.client.ReactorClientHttpRequestFactory
import reactor.netty.http.client.HttpClient
import java.util.function.Consumer
import java.util.function.UnaryOperator

public interface CustomReactorNettyClientHttpRequestFactoryBuilder :
    CustomClientHttpRequestFactoryBuilder<ReactorClientHttpRequestFactoryBuilder, ReactorClientHttpRequestFactory, CustomReactorNettyClientHttpRequestFactoryBuilder> {

    public fun httpClientCustomizer(customizer: UnaryOperator<HttpClient>): CustomReactorNettyClientHttpRequestFactoryBuilder

    public fun clientHttpRequestFactoryConfigurer(configurer: ReactorClientHttpRequestFactoryConfigurer): CustomReactorNettyClientHttpRequestFactoryBuilder
}

public class DefaultCustomReactorNettyClientHttpRequestFactoryBuilder : CustomReactorNettyClientHttpRequestFactoryBuilder {
    private var httpClientCustomizer: UnaryOperator<HttpClient> = UnaryOperator.identity()

    private val clientHttpRequestFactoryCustomizers: MutableList<Consumer<ReactorClientHttpRequestFactory>> = mutableListOf()
    private var clientHttpRequestFactoryConfigurer: ReactorClientHttpRequestFactoryConfigurer? = null

    private var settings: ClientHttpRequestFactorySettings? = null

    override fun httpClientCustomizer(customizer: UnaryOperator<HttpClient>): DefaultCustomReactorNettyClientHttpRequestFactoryBuilder = apply {
        this.httpClientCustomizer = UnaryOperator<HttpClient> { customizer.apply(httpClientCustomizer.apply(it)) }
    }

    override fun clientHttpRequestFactoryConfigurer(configurer: ReactorClientHttpRequestFactoryConfigurer): DefaultCustomReactorNettyClientHttpRequestFactoryBuilder =
        apply {
        this.clientHttpRequestFactoryConfigurer = configurer
    }

    override fun customizer(customizer: Consumer<ReactorClientHttpRequestFactory>): DefaultCustomReactorNettyClientHttpRequestFactoryBuilder = apply {
        clientHttpRequestFactoryCustomizers.add(customizer)
    }

    override fun settings(settings: ClientHttpRequestFactorySettings): DefaultCustomReactorNettyClientHttpRequestFactoryBuilder = apply {
        this.settings = settings
    }

    override fun applyOther(builderConsumer: Consumer<CustomReactorNettyClientHttpRequestFactoryBuilder>): DefaultCustomReactorNettyClientHttpRequestFactoryBuilder =
        apply {
        builderConsumer.accept(this)
    }

    override fun build(builder: ReactorClientHttpRequestFactoryBuilder): ReactorClientHttpRequestFactory = builder
        .withHttpClientCustomizer(httpClientCustomizer)
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