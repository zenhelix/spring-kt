package test.globalspringconfig

import io.github.zenhelix.spring.autoconfiguration.web.client.apache.ApacheRequestConfigBuilderCustomizer
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
import test.globalspringconfig.YmlOverrideTestClientHttpProperties.Companion.YML_OVERRIDE_TEST_CLIENT

@SpringBootApplication
class YmlOverrideApacheClientConfigurationApplicationTest {
    fun main(args: Array<String>) {
        runApplication<YmlOverrideApacheClientConfigurationApplicationTest>(*args)
    }
}

@Configuration(proxyBeanMethods = false)
class ClientTestConfiguration {

    @Bean fun disableProtocolUpgradeCustomizer() = ApacheRequestConfigBuilderCustomizer {
        it.setProtocolUpgradeEnabled(false)
    }

}

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(YmlOverrideTestClientHttpProperties::class)
class YmlOverrideTestClientConfiguration(private val properties: YmlOverrideTestClientHttpProperties) {

    @Bean
    fun overrideYamlGlobalTestHttpClient(
        builder: CustomHttpComponentsClientHttpRequestFactoryBuilder,
        clientHttpRequestFactorySettings: ObjectProvider<ClientHttpRequestFactorySettings>
    ) = httpComponentsClientHttpRequestFactory(properties.http, builder, clientHttpRequestFactorySettings.ifAvailable)

}

@Validated
@ConfigurationProperties(YML_OVERRIDE_TEST_CLIENT, ignoreUnknownFields = false)
class YmlOverrideTestClientHttpProperties {

    @NestedConfigurationProperty
    val http: ApacheHttpClientProperties = ApacheHttpClientProperties()

    companion object {
        const val YML_OVERRIDE_TEST_CLIENT = "test.client.global.yml"
    }
}
