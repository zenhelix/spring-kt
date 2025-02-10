package io.github.zenhelix.spring.autoconfiguration.web.client.reactor.netty

import org.springframework.http.client.ReactorClientHttpRequestFactory
import java.util.function.Consumer

public class ReactorClientHttpRequestFactoryConfigurer(private val customizers: List<Consumer<ReactorClientHttpRequestFactory>>? = null) {
    public fun accumulate(): Consumer<ReactorClientHttpRequestFactory>? = customizers?.takeIf { it.isNotEmpty() }?.let {
        it.reduce { acc, customizer -> acc.andThen(customizer) }
    }

    public fun configure(builder: ReactorClientHttpRequestFactory): ReactorClientHttpRequestFactory {
        accumulate()?.accept(builder)
        return builder
    }
}

