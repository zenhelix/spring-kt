package io.github.zenhelix.spring.autoconfiguration.web.client.properties.apache

import io.github.zenhelix.spring.autoconfiguration.web.client.properties.AbstractHttpClientProperties.ProxyProperties
import io.github.zenhelix.spring.autoconfiguration.web.client.properties.apache.ApacheHttpClientProperties.PoolProperties
import io.github.zenhelix.spring.boot.configurationprocessor.test.CompiledMetadataReader.compileMetadata
import io.github.zenhelix.spring.testing.assertion.spring.processor.Metadata.withProperty
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.assertj.core.api.Assertions.assertThat
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator
import org.junit.jupiter.api.Test
import org.springframework.core.test.tools.SourceFile.forTestClass
import org.springframework.core.test.tools.TestCompiler.forSystem
import test.SomeApacheHttpClientProperties
import java.time.Duration

class ApacheHttpClientPropertiesTest {
    private val validator: Validator = Validation.byDefaultProvider()
        .configure()
        .messageInterpolator(ParameterMessageInterpolator())
        .buildValidatorFactory().usingContext()
        .validator

    @Test fun `properties metadata`() {
        val metadata = compileMetadata(forSystem().withSources(forTestClass(SomeApacheHttpClientProperties::class.java)))

        assertThat(metadata)
            .has(withProperty("some-apache-client.http.base-url"))
            .has(withProperty("some-apache-client.http.connect-timeout").withDefaultValue("3s"))
            .has(withProperty("some-apache-client.http.read-timeout").withDefaultValue("10s"))
            .has(withProperty("some-apache-client.http.use-proxy").withDefaultValue(false))
            .has(withProperty("some-apache-client.http.proxy.proxy-host"))
            .has(withProperty("some-apache-client.http.proxy.proxy-port"))
            .has(withProperty("some-apache-client.http.proxy.use-system")/*.withDefaultValue(true) FIXME*/)

            .has(withProperty("some-apache-client.http.connection-manager-shared"))
            .has(withProperty("some-apache-client.http.request.connection-request-timeout"))
            .has(withProperty("some-apache-client.http.pool.connection.connection-time-to-live"))
            .has(withProperty("some-apache-client.http.pool.max-connections"))
    }

    @Test fun `validate properties metadata`() {
        val properties = SomeApacheHttpClientProperties().apply {
            http = ApacheHttpClientProperties(
                "",
                Duration.ofSeconds(3), Duration.ofSeconds(10), false, ProxyProperties("", null, true),
                null, null,
                PoolProperties(-1, null, null, null)
            )
        }

        val constraintViolations = validator.validate(properties)
        assertThat(constraintViolations).hasSize(3)
        assertThat(constraintViolations.map { it.propertyPath.toString() to it.message }).containsExactlyInAnyOrder(
            "http.baseUrl" to "{io.github.zenhelix.validation.constraints.NullableNotBlank.message}",
            "http.pool.maxConnections" to "must be greater than 0",
            "http.proxy.proxyHost" to "must not be blank"
        )
    }

}