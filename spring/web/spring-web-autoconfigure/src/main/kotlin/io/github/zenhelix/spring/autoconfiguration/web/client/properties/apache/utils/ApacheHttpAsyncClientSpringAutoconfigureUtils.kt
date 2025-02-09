package io.github.zenhelix.spring.autoconfiguration.web.client.properties.apache.utils

import io.github.zenhelix.spring.autoconfiguration.web.client.properties.apache.ApacheHttpAsyncClientProperties
import io.github.zenhelix.spring.autoconfiguration.web.client.properties.apache.utils.ApacheHttpClientSpringAutoconfigureUtils.toProperties
import io.github.zenhelix.web.http.client.apache.ApacheHttpAsyncClientFactory.apacheHttpAsyncClient
import io.github.zenhelix.web.http.client.apache.ApacheHttpAsyncClientFactory.poolingAsyncClientConnectionManager
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder
import org.apache.hc.core5.http.nio.ssl.BasicClientTlsStrategy
import org.apache.hc.core5.http.nio.ssl.TlsStrategy
import org.springframework.boot.ssl.SslBundle
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.http.client.reactive.HttpComponentsClientHttpConnector

public object ApacheHttpAsyncClientSpringAutoconfigureUtils {

    public fun apacheHttpAsyncClient(
        properties: ApacheHttpAsyncClientProperties, sslBundle: SslBundle? = null,
        builder: HttpAsyncClientBuilder = HttpAsyncClientBuilder.create(), initializer: HttpAsyncClientBuilder.() -> Unit = {}
    ): CloseableHttpAsyncClient = apacheHttpAsyncClient(properties.toProperties(), builder = builder) {
        if (sslBundle != null) {
            properties.toProperties().poolingConnectionManager?.let {
                poolingAsyncClientConnectionManager(it) {
                    val tlsStrategy: TlsStrategy = BasicClientTlsStrategy(sslBundle.createSslContext()) { _, sslEngine ->
                        sslBundle.options.ciphers?.also { sslEngine.enabledCipherSuites = it }
                        sslBundle.options.enabledProtocols?.also { sslEngine.enabledProtocols = it }
                        null
                    }
                    setTlsStrategy(tlsStrategy)
                }
            }?.also { setConnectionManager(it) }
        }

        apply(initializer)
    }

    public fun httpComponentsClientHttpConnector(
        properties: ApacheHttpAsyncClientProperties, sslBundle: SslBundle? = null, bufferFactory: DataBufferFactory? = null,
        builder: HttpAsyncClientBuilder = HttpAsyncClientBuilder.create(), initializer: HttpAsyncClientBuilder.() -> Unit = {}
    ): HttpComponentsClientHttpConnector = HttpComponentsClientHttpConnector(
        apacheHttpAsyncClient(properties, sslBundle, builder, initializer)
    ).apply { bufferFactory?.let { setBufferFactory(it) } }

}