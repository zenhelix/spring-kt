package test.globalconfig

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
import test.globalconfig.GlobalTestClientHttpProperties.Companion.GLOBAL_TEST_CLIENT

@SpringBootApplication
class GlobalApacheClientConfigurationApplicationTest {
    fun main(args: Array<String>) {
        runApplication<GlobalApacheClientConfigurationApplicationTest>(*args)
    }
}

@Configuration(proxyBeanMethods = false)
class ClientTestConfiguration {

    @Bean fun disableProtocolUpgradeCustomizer() = ApacheRequestConfigBuilderCustomizer {
        it.setProtocolUpgradeEnabled(false)
    }

}

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(GlobalTestClientHttpProperties::class)
class GlobalTestClientConfiguration(private val properties: GlobalTestClientHttpProperties) {

    @Bean
    fun globalTestHttpClient(
        builder: CustomHttpComponentsClientHttpRequestFactoryBuilder,
        clientHttpRequestFactorySettings: ObjectProvider<ClientHttpRequestFactorySettings>
    ) = httpComponentsClientHttpRequestFactory(properties.http, builder, clientHttpRequestFactorySettings.ifAvailable)

}

@Validated
@ConfigurationProperties(GLOBAL_TEST_CLIENT, ignoreUnknownFields = false)
class GlobalTestClientHttpProperties {

    @NestedConfigurationProperty
    val http: ApacheHttpClientProperties = ApacheHttpClientProperties()

    companion object {
        const val GLOBAL_TEST_CLIENT = "test.client.global"
    }
}
