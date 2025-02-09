package test;

import io.github.zenhelix.spring.autoconfiguration.web.client.properties.apache.ApacheHttpClientProperties;
import jakarta.validation.Valid;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

@SuppressWarnings("ConfigurationProperties")
@Validated
@ConfigurationProperties(prefix = "some-apache-client")
public class SomeApacheHttpClientProperties {

    @NestedConfigurationProperty
    @Valid
    private ApacheHttpClientProperties http;

    public ApacheHttpClientProperties getHttp() {
        return http;
    }

    public void setHttp(ApacheHttpClientProperties http) {
        this.http = http;
    }

}
