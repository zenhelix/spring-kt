package io.github.zenhelix.spring.autoconfiguration.web.client.reactor.netty

import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.http.client.ReactorClientHttpRequestFactory
import reactor.netty.http.client.HttpClient
import java.util.function.Consumer
import java.util.function.UnaryOperator

@AutoConfiguration
@ConditionalOnClass(HttpClient::class)
public class ReactorNettyHttpClientSpringAutoconfigure {

    @Bean
    @ConditionalOnMissingBean
    public fun reactorClientHttpRequestFactoryConfigurer(
        customizerProvider: ObjectProvider<Consumer<ReactorClientHttpRequestFactory>>
    ): ReactorClientHttpRequestFactoryConfigurer = ReactorClientHttpRequestFactoryConfigurer(customizerProvider.orderedStream().toList())

    /** Возвращаем обертку над билдером, чтоб не ломать логику самого спринга при автоваринге */
    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    public fun customJettyClientHttpRequestFactoryBuilder(
        httpClientCustomizer: ObjectProvider<UnaryOperator<HttpClient>>,
        reactorClientHttpRequestFactoryConfigurer: ReactorClientHttpRequestFactoryConfigurer
    ): DefaultCustomReactorNettyClientHttpRequestFactoryBuilder = DefaultCustomReactorNettyClientHttpRequestFactoryBuilder()
        .let { builder ->
            httpClientCustomizer.ifUnique?.let {
                builder.httpClientCustomizer(it)
            } ?: builder
        }
        .clientHttpRequestFactoryConfigurer(reactorClientHttpRequestFactoryConfigurer)

}