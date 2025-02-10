package io.github.zenhelix.spring.autoconfiguration.web.client

import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings
import org.springframework.http.client.ClientHttpRequestFactory
import java.util.function.Consumer

public interface CustomClientHttpRequestFactoryBuilder<
        B : ClientHttpRequestFactoryBuilder<out ClientHttpRequestFactory>,
        C : ClientHttpRequestFactory,
        T : CustomClientHttpRequestFactoryBuilder<B, C, T>
        > {

 public fun customizer(customizer: Consumer<C>): T
 public fun settings(settings: ClientHttpRequestFactorySettings): T
 public fun applyOther(builderConsumer: Consumer<T>): T
 public fun build(builder: B): C

}