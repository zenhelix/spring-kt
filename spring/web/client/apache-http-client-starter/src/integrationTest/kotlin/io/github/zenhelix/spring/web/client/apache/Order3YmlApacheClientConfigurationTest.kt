package io.github.zenhelix.spring.web.client.apache

import org.apache.hc.client5.http.config.Configurable
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.test.context.ActiveProfiles
import test.ymlconfig.YamlApacheClientConfigurationApplicationTest

@ActiveProfiles("testyml")
@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    classes = [YamlApacheClientConfigurationApplicationTest::class]
)
class Order3YmlApacheClientConfigurationTest(@Autowired val requestFactory: HttpComponentsClientHttpRequestFactory) {

    @Test fun `override from yml properties`() {
        val httpClientConfigurable = requestFactory.httpClient as Configurable
        assertThat(httpClientConfigurable.config.isProtocolUpgradeEnabled).isEqualTo(false)
    }

}