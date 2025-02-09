package io.github.zenhelix.spring.autoconfiguration.web.client.jdk

import org.springframework.http.client.JdkClientHttpRequestFactory
import java.net.http.HttpClient
import java.util.function.Consumer

public class JdkHttpClientBuilderConfigurer(private val customizers: List<JdkHttpClientBuilderCustomizer>? = null) {
    public fun accumulate(): JdkHttpClientBuilderCustomizer? = customizers?.takeIf { it.isNotEmpty() }?.let {
        it.reduce { acc, customizer -> acc.andThen(customizer) }
    }

    public fun configure(builder: HttpClient.Builder): HttpClient.Builder {
        accumulate()?.customize(builder)
        return builder
    }
}

public class JdkClientHttpRequestFactoryConfigurer(private val customizers: List<Consumer<JdkClientHttpRequestFactory>>? = null) {
    public fun accumulate(): Consumer<JdkClientHttpRequestFactory>? = customizers?.takeIf { it.isNotEmpty() }?.let {
        it.reduce { acc, customizer -> acc.andThen(customizer) }
    }

    public fun configure(builder: JdkClientHttpRequestFactory): JdkClientHttpRequestFactory {
        accumulate()?.accept(builder)
        return builder
    }
}

public fun interface JdkHttpClientBuilderCustomizer {
    public fun customize(builder: HttpClient.Builder)
    public fun andThen(after: JdkHttpClientBuilderCustomizer): JdkHttpClientBuilderCustomizer = JdkHttpClientBuilderCustomizer {
        customize(it)
        after.customize(it)
    }
}
