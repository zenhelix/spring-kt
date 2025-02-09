package io.github.zenhelix.spring.autoconfiguration.web.client.jetty

import org.eclipse.jetty.client.HttpClient
import org.eclipse.jetty.client.HttpClientTransport
import org.eclipse.jetty.io.ClientConnector
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.http.client.JettyClientHttpRequestFactory
import java.util.function.Consumer

@AutoConfiguration
@ConditionalOnClass(HttpClient::class)
public class JettyHttpClientSpringAutoconfigure {

    @Bean
    @ConditionalOnMissingBean
    public fun jettyHttpClientConfigurer(
        customizerProvider: ObjectProvider<Consumer<HttpClient>>
    ): JettyHttpClientConfigurer = JettyHttpClientConfigurer(customizerProvider.orderedStream().toList())

    @Bean
    @ConditionalOnMissingBean
    public fun jettyHttpClientTransportConfigurer(
        customizerProvider: ObjectProvider<Consumer<HttpClientTransport>>
    ): JettyHttpClientTransportConfigurer = JettyHttpClientTransportConfigurer(customizerProvider.orderedStream().toList())

    @Bean
    @ConditionalOnMissingBean
    public fun jettyClientConnectorConfigurer(
        customizerProvider: ObjectProvider<Consumer<ClientConnector>>
    ): JettyClientConnectorConfigurer = JettyClientConnectorConfigurer(customizerProvider.orderedStream().toList())

    @Bean
    @ConditionalOnMissingBean
    public fun jettyClientHttpRequestFactoryConfigurer(
        customizerProvider: ObjectProvider<Consumer<JettyClientHttpRequestFactory>>
    ): JettyClientHttpRequestFactoryConfigurer = JettyClientHttpRequestFactoryConfigurer(customizerProvider.orderedStream().toList())

    /** Возвращаем обертку над билдером, чтоб не ломать логику самого спринга при автоваринге */
    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    public fun customJettyClientHttpRequestFactoryBuilder(
        jettyHttpClientConfigurer: JettyHttpClientConfigurer,
        jettyHttpClientTransportConfigurer: JettyHttpClientTransportConfigurer,
        jettyClientConnectorConfigurer: JettyClientConnectorConfigurer,
        jettyClientHttpRequestFactoryConfigurer: JettyClientHttpRequestFactoryConfigurer
    ): DefaultCustomJettyClientHttpRequestFactoryBuilder = DefaultCustomJettyClientHttpRequestFactoryBuilder()
        .httpClientBuilderConfigurer(jettyHttpClientConfigurer)
        .httpClientTransportConfigurer(jettyHttpClientTransportConfigurer)
        .clientConnectorConfigurer(jettyClientConnectorConfigurer)
        .clientHttpRequestFactoryConfigurer(jettyClientHttpRequestFactoryConfigurer)

}