package io.github.zenhelix.spring.autoconfiguration.web.client.jdk

import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.http.client.JdkClientHttpRequestFactory
import java.util.function.Consumer

@AutoConfiguration
public class JdkHttpClientSpringAutoconfigure {

    @Bean
    @ConditionalOnMissingBean
    public fun jdkHttpClientBuilderConfigurer(
        customizerProvider: ObjectProvider<JdkHttpClientBuilderCustomizer>
    ): JdkHttpClientBuilderConfigurer = JdkHttpClientBuilderConfigurer(customizerProvider.orderedStream().toList())

    @Bean
    @ConditionalOnMissingBean
    public fun jdkClientHttpRequestFactoryConfigurer(
        customizerProvider: ObjectProvider<Consumer<JdkClientHttpRequestFactory>>
    ): JdkClientHttpRequestFactoryConfigurer = JdkClientHttpRequestFactoryConfigurer(customizerProvider.orderedStream().toList())

    /** Возвращаем обертку над билдером, чтоб не ломать логику самого спринга при автоваринге */
    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    public fun customJdkClientHttpRequestFactoryBuilder(
        jdkHttpClientBuilderConfigurer: JdkHttpClientBuilderConfigurer,
        jdkClientHttpRequestFactoryConfigurer: JdkClientHttpRequestFactoryConfigurer
    ): DefaultCustomJdkClientHttpRequestFactoryBuilder = DefaultCustomJdkClientHttpRequestFactoryBuilder()
        .httpClientBuilderConfigurer(jdkHttpClientBuilderConfigurer)
        .clientHttpRequestFactoryConfigurer(jdkClientHttpRequestFactoryConfigurer)

}