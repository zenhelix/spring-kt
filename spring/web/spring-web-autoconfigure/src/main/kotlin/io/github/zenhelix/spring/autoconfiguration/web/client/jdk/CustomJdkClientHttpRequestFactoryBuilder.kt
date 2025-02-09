package io.github.zenhelix.spring.autoconfiguration.web.client.jdk

import io.github.zenhelix.spring.autoconfiguration.web.client.CustomClientHttpRequestFactoryBuilder
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings
import org.springframework.boot.http.client.JdkClientHttpRequestFactoryBuilder
import org.springframework.http.client.JdkClientHttpRequestFactory
import java.net.http.HttpClient
import java.util.function.Consumer

public interface CustomJdkClientHttpRequestFactoryBuilder :
    CustomClientHttpRequestFactoryBuilder<JdkClientHttpRequestFactoryBuilder, JdkClientHttpRequestFactory, CustomJdkClientHttpRequestFactoryBuilder> {

    public fun httpClientCustomizer(customizer: Consumer<HttpClient.Builder>): CustomJdkClientHttpRequestFactoryBuilder
    public fun httpClientBuilderConfigurer(configurer: JdkHttpClientBuilderConfigurer): CustomJdkClientHttpRequestFactoryBuilder

    public fun clientHttpRequestFactoryConfigurer(configurer: JdkClientHttpRequestFactoryConfigurer): CustomJdkClientHttpRequestFactoryBuilder
}

public class DefaultCustomJdkClientHttpRequestFactoryBuilder : CustomJdkClientHttpRequestFactoryBuilder {
    private val httpClientCustomizers: MutableList<Consumer<HttpClient.Builder>> = mutableListOf()
    private var httpClientBuilderConfigurer: JdkHttpClientBuilderConfigurer? = null

    private val clientHttpRequestFactoryCustomizers: MutableList<Consumer<JdkClientHttpRequestFactory>> = mutableListOf()
    private var clientHttpRequestFactoryConfigurer: JdkClientHttpRequestFactoryConfigurer? = null

    private var settings: ClientHttpRequestFactorySettings? = null

    override fun httpClientCustomizer(customizer: Consumer<HttpClient.Builder>): DefaultCustomJdkClientHttpRequestFactoryBuilder = apply {
        httpClientCustomizers.add(customizer)
    }

    override fun httpClientBuilderConfigurer(configurer: JdkHttpClientBuilderConfigurer): DefaultCustomJdkClientHttpRequestFactoryBuilder = apply {
        this.httpClientBuilderConfigurer = configurer
    }

    override fun clientHttpRequestFactoryConfigurer(configurer: JdkClientHttpRequestFactoryConfigurer): DefaultCustomJdkClientHttpRequestFactoryBuilder =
        apply {
        this.clientHttpRequestFactoryConfigurer = configurer
    }

    override fun customizer(customizer: Consumer<JdkClientHttpRequestFactory>): DefaultCustomJdkClientHttpRequestFactoryBuilder = apply {
        clientHttpRequestFactoryCustomizers.add(customizer)
    }

    override fun settings(settings: ClientHttpRequestFactorySettings): DefaultCustomJdkClientHttpRequestFactoryBuilder = apply {
        this.settings = settings
    }

    override fun applyOther(builderConsumer: Consumer<CustomJdkClientHttpRequestFactoryBuilder>): DefaultCustomJdkClientHttpRequestFactoryBuilder = apply {
        builderConsumer.accept(this)
    }

    override fun build(builder: JdkClientHttpRequestFactoryBuilder): JdkClientHttpRequestFactory = builder
        .let {
            httpClientBuilderConfigurer?.accumulate()?.also { c -> httpClientCustomizers.add(0, Consumer { c.customize(it) }) }
            if (httpClientCustomizers.isNotEmpty()) {
                it.withHttpClientCustomizer(httpClientCustomizers.reduce { acc, consumer -> acc.andThen(consumer) })
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