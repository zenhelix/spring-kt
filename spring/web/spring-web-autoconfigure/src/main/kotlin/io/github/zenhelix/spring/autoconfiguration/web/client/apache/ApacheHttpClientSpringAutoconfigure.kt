package io.github.zenhelix.spring.autoconfiguration.web.client.apache

import org.apache.hc.client5.http.classic.HttpClient
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import java.util.function.Consumer

@AutoConfiguration
@ConditionalOnClass(HttpClient::class)
public class ApacheHttpClientSpringAutoconfigure {

    @Bean
    @ConditionalOnMissingBean
    public fun apacheHttpClientBuilderConfigurer(
        customizerProvider: ObjectProvider<ApacheHttpClientBuilderCustomizer>
    ): ApacheHttpClientBuilderConfigurer = ApacheHttpClientBuilderConfigurer(customizerProvider.orderedStream().toList())

    @Bean
    @ConditionalOnMissingBean
    public fun apacheRequestConfigBuilderConfigurer(
        customizerProvider: ObjectProvider<ApacheRequestConfigBuilderCustomizer>
    ): ApacheRequestConfigBuilderConfigurer = ApacheRequestConfigBuilderConfigurer(customizerProvider.orderedStream().toList())

    @Bean
    @ConditionalOnMissingBean
    public fun apacheSocketConfigBuilderConfigurer(
        customizerProvider: ObjectProvider<ApacheSocketConfigBuilderCustomizer>
    ): ApacheSocketConfigBuilderConfigurer = ApacheSocketConfigBuilderConfigurer(customizerProvider.orderedStream().toList())

    @Bean
    @ConditionalOnMissingBean
    public fun apachePoolingHttpClientConnectionManagerBuilderConfigurer(
        customizerProvider: ObjectProvider<ApachePoolingHttpClientConnectionManagerBuilderCustomizer>
    ): ApachePoolingHttpClientConnectionManagerBuilderConfigurer =
        ApachePoolingHttpClientConnectionManagerBuilderConfigurer(customizerProvider.orderedStream().toList())

    @Bean
    @ConditionalOnMissingBean
    public fun httpComponentsClientHttpRequestFactoryConfigurer(
        customizerProvider: ObjectProvider<Consumer<HttpComponentsClientHttpRequestFactory>>
    ): HttpComponentsClientHttpRequestFactoryConfigurer = HttpComponentsClientHttpRequestFactoryConfigurer(customizerProvider.orderedStream().toList())

    /** Возвращаем обертку над билдером, чтоб не ломать логику самого спринга при автоваринге */
    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    public fun customHttpComponentsClientHttpRequestFactoryBuilder(
        apacheHttpClientBuilderConfigurer: ApacheHttpClientBuilderConfigurer,
        apacheRequestConfigBuilderConfigurer: ApacheRequestConfigBuilderConfigurer,
        apacheSocketConfigBuilderConfigurer: ApacheSocketConfigBuilderConfigurer,
        apachePoolingHttpClientConnectionManagerBuilderConfigurer: ApachePoolingHttpClientConnectionManagerBuilderConfigurer,
        httpComponentsClientHttpRequestFactoryConfigurer: HttpComponentsClientHttpRequestFactoryConfigurer
    ): DefaultCustomHttpComponentsClientHttpRequestFactoryBuilder = DefaultCustomHttpComponentsClientHttpRequestFactoryBuilder()
        .httpClientBuilderConfigurer(apacheHttpClientBuilderConfigurer)
        .requestConfigBuilderConfigurer(apacheRequestConfigBuilderConfigurer)
        .poolingHttpClientConnectionManagerBuilderConfigurer(apachePoolingHttpClientConnectionManagerBuilderConfigurer)
        .socketConfigBuilderConfigurer(apacheSocketConfigBuilderConfigurer)
        .clientHttpRequestFactoryConfigurer(httpComponentsClientHttpRequestFactoryConfigurer)

}