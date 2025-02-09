package test.defaultconfig

import io.github.zenhelix.spring.autoconfiguration.web.client.apache.CustomHttpComponentsClientHttpRequestFactoryBuilder
import io.github.zenhelix.spring.autoconfiguration.web.client.properties.apache.ApacheHttpClientProperties
import io.github.zenhelix.spring.autoconfiguration.web.client.properties.apache.utils.ApacheHttpClientSpringAutoconfigureUtils.httpComponentsClientHttpRequestFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated
import test.defaultconfig.TestDefaultClientHttpProperties.Companion.DEFAULT_TEST_CLIENT

@SpringBootApplication
class DefaultApacheClientConfigurationApplicationTest {
    fun main(args: Array<String>) {
        runApplication<DefaultApacheClientConfigurationApplicationTest>(*args)
    }
}

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(TestDefaultClientHttpProperties::class)
class DefaultTestClientConfiguration(private val properties: TestDefaultClientHttpProperties) {

    @Bean
    fun defaultTestHttpClient(
        builder: CustomHttpComponentsClientHttpRequestFactoryBuilder,
        clientHttpRequestFactorySettings: ObjectProvider<ClientHttpRequestFactorySettings>
    ) = httpComponentsClientHttpRequestFactory(properties.http, builder, clientHttpRequestFactorySettings.ifAvailable)

}

@Validated
@ConfigurationProperties(DEFAULT_TEST_CLIENT, ignoreUnknownFields = false)
class TestDefaultClientHttpProperties {

    @NestedConfigurationProperty
    val http: ApacheHttpClientProperties = ApacheHttpClientProperties()

    companion object {
        const val DEFAULT_TEST_CLIENT = "test.client.default"
    }
}
