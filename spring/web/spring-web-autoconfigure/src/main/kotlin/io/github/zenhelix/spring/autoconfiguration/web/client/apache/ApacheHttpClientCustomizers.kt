package io.github.zenhelix.spring.autoconfiguration.web.client.apache

import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.core5.http.io.SocketConfig
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import java.util.function.Consumer

public class ApacheHttpClientBuilderConfigurer(private val customizers: List<ApacheHttpClientBuilderCustomizer>? = null) {
    public fun accumulate(): ApacheHttpClientBuilderCustomizer? = customizers?.takeIf { it.isNotEmpty() }?.let {
        it.reduce { acc, customizer -> acc.andThen(customizer) }
    }

    public fun configure(builder: HttpClientBuilder): HttpClientBuilder {
        accumulate()?.customize(builder)
        return builder
    }
}

public class ApacheRequestConfigBuilderConfigurer(private val customizers: List<ApacheRequestConfigBuilderCustomizer>? = null) {
    public fun accumulate(): ApacheRequestConfigBuilderCustomizer? = customizers?.takeIf { it.isNotEmpty() }?.let {
        it.reduce { acc, customizer -> acc.andThen(customizer) }
    }

    public fun configure(builder: RequestConfig.Builder): RequestConfig.Builder {
        accumulate()?.customize(builder)
        return builder
    }
}

public class ApacheSocketConfigBuilderConfigurer(private val customizers: List<ApacheSocketConfigBuilderCustomizer>? = null) {
    public fun accumulate(): ApacheSocketConfigBuilderCustomizer? = customizers?.takeIf { it.isNotEmpty() }?.let {
        it.reduce { acc, customizer -> acc.andThen(customizer) }
    }

    public fun configure(builder: SocketConfig.Builder): SocketConfig.Builder {
        accumulate()?.customize(builder)
        return builder
    }
}

public class ApachePoolingHttpClientConnectionManagerBuilderConfigurer(private val customizers: List<ApachePoolingHttpClientConnectionManagerBuilderCustomizer>? = null) {
    public fun accumulate(): ApachePoolingHttpClientConnectionManagerBuilderCustomizer? = customizers?.takeIf { it.isNotEmpty() }?.let {
        it.reduce { acc, customizer -> acc.andThen(customizer) }
    }

    public fun configure(builder: PoolingHttpClientConnectionManagerBuilder): PoolingHttpClientConnectionManagerBuilder {
        accumulate()?.customize(builder)
        return builder
    }
}

public class HttpComponentsClientHttpRequestFactoryConfigurer(private val customizers: List<Consumer<HttpComponentsClientHttpRequestFactory>>? = null) {
    public fun accumulate(): Consumer<HttpComponentsClientHttpRequestFactory>? = customizers?.takeIf { it.isNotEmpty() }?.let {
        it.reduce { acc, customizer -> acc.andThen(customizer) }
    }

    public fun configure(builder: HttpComponentsClientHttpRequestFactory): HttpComponentsClientHttpRequestFactory {
        accumulate()?.accept(builder)
        return builder
    }
}

public fun interface ApacheHttpClientBuilderCustomizer {
    public fun customize(builder: HttpClientBuilder)
    public fun andThen(after: ApacheHttpClientBuilderCustomizer): ApacheHttpClientBuilderCustomizer = ApacheHttpClientBuilderCustomizer {
        customize(it)
        after.customize(it)
    }
}

public fun interface ApacheRequestConfigBuilderCustomizer {
    public fun customize(builder: RequestConfig.Builder)
    public fun andThen(after: ApacheRequestConfigBuilderCustomizer): ApacheRequestConfigBuilderCustomizer = ApacheRequestConfigBuilderCustomizer {
        customize(it)
        after.customize(it)
    }
}

public fun interface ApacheSocketConfigBuilderCustomizer {
    public fun customize(builder: SocketConfig.Builder)
    public fun andThen(after: ApacheSocketConfigBuilderCustomizer): ApacheSocketConfigBuilderCustomizer = ApacheSocketConfigBuilderCustomizer {
        customize(it)
        after.customize(it)
    }
}

public fun interface ApachePoolingHttpClientConnectionManagerBuilderCustomizer {
    public fun customize(builder: PoolingHttpClientConnectionManagerBuilder)
    public fun andThen(after: ApachePoolingHttpClientConnectionManagerBuilderCustomizer): ApachePoolingHttpClientConnectionManagerBuilderCustomizer =
        ApachePoolingHttpClientConnectionManagerBuilderCustomizer {
        customize(it)
        after.customize(it)
    }
}
