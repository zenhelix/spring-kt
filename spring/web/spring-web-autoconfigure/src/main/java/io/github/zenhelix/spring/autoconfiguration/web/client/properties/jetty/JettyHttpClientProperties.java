package io.github.zenhelix.spring.autoconfiguration.web.client.properties.jetty;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import io.github.zenhelix.spring.autoconfiguration.web.client.properties.AbstractHttpClientProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.boot.convert.DurationUnit;

public class JettyHttpClientProperties extends AbstractHttpClientProperties {

    private Boolean ignoreSsl;

    public JettyHttpClientProperties() {
        super();
        this.ignoreSsl = null;
    }

    @ConstructorBinding
    public JettyHttpClientProperties(
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

            Boolean ignoreSsl
    ) {
        super(baseUrl, connectTimeout, readTimeout, useProxy, proxy);
        this.ignoreSsl = ignoreSsl;
    }

    public Boolean getIgnoreSsl() {
        return ignoreSsl;
    }

    public void setIgnoreSsl(Boolean ignoreSsl) {
        this.ignoreSsl = ignoreSsl;
    }

}