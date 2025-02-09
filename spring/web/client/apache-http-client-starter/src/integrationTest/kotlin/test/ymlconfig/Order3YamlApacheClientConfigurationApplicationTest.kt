package test.ymlconfig

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
import test.ymlconfig.YamlTestClientHttpProperties.Companion.YAML_TEST_CLIENT

@SpringBootApplication
class YamlApacheClientConfigurationApplicationTest {
    fun main(args: Array<String>) {
        runApplication<YamlApacheClientConfigurationApplicationTest>(*args)
    }
}

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(YamlTestClientHttpProperties::class)
class YamlTestClientConfiguration(private val properties: YamlTestClientHttpProperties) {

    @Bean
    fun yamlTestHttpClient(
        builder: CustomHttpComponentsClientHttpRequestFactoryBuilder,
        clientHttpRequestFactorySettings: ObjectProvider<ClientHttpRequestFactorySettings>
    ) = httpComponentsClientHttpRequestFactory(properties.http, builder, clientHttpRequestFactorySettings.ifAvailable)

}

@Validated
@ConfigurationProperties(YAML_TEST_CLIENT, ignoreUnknownFields = false)
class YamlTestClientHttpProperties {

    @NestedConfigurationProperty
    val http: ApacheHttpClientProperties = ApacheHttpClientProperties()

    companion object {
        const val YAML_TEST_CLIENT = "test.client.yml"
    }
}
