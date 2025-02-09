package io.github.zenhelix.spring.autoconfiguration.web.client.properties.jdk;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import io.github.zenhelix.spring.autoconfiguration.web.client.properties.AbstractHttpClientProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.boot.convert.DurationUnit;

public class JdkHttpClientProperties extends AbstractHttpClientProperties {

    private HttpClient.Redirect redirectType;

    public JdkHttpClientProperties() {
        super();
        this.redirectType = null;
    }

    @ConstructorBinding
    public JdkHttpClientProperties(
            String baseUrl,
            @DefaultValue("3s")
            @DurationUnit(ChronoUnit.SECONDS)
            Duration connectTimeout,
            @DefaultValue("10s")
            @DurationUnit(ChronoUnit.SECONDS)
            Duration readTimeout,
            @DefaultValue("false")
            Boolean useProxy,
            ProxyProperties proxy,

            HttpClient.Redirect redirectType
    ) {
        super(baseUrl, connectTimeout, readTimeout, useProxy, proxy);
        this.redirectType = redirectType;
    }

    public HttpClient.Redirect getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(HttpClient.Redirect redirectType) {
        this.redirectType = redirectType;
    }

}
