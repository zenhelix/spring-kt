package io.github.zenhelix.spring.autoconfiguration.web.client.properties;

import java.time.Duration;

import io.github.zenhelix.validation.constraints.NullableNotBlank;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

public abstract class AbstractHttpClientProperties {

    /** Базовый url для сервиса */
    @NullableNotBlank
    private String baseUrl;
    @NotNull
    @DurationMin(millis = 0)
    private Duration connectTimeout;
    @NotNull
    @DurationMin(millis = 0)
    private Duration readTimeout;
    @NotNull
    private Boolean useProxy;
    @Valid
    private ProxyProperties proxy;

    public AbstractHttpClientProperties() {
        this.baseUrl = null;
        this.connectTimeout = Duration.ofSeconds(3);
        this.readTimeout = Duration.ofSeconds(10);
        this.useProxy = false;
        this.proxy = null;
    }

    @ConstructorBinding
    public AbstractHttpClientProperties(
            String baseUrl,
            Duration connectTimeout,
            Duration readTimeout,
            Boolean useProxy,
            ProxyProperties proxy
    ) {
        this.baseUrl = baseUrl;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.useProxy = useProxy;
        this.proxy = proxy;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Boolean getUseProxy() {
        return useProxy;
    }

    public void setUseProxy(Boolean useProxy) {
        this.useProxy = useProxy;
    }

    public ProxyProperties getProxy() {
        return proxy;
    }

    public void setProxy(ProxyProperties proxy) {
        this.proxy = proxy;
    }

    public record ProxyProperties(
            @NotBlank
            String proxyHost,
            @Range(min = 1, max = 65535)
            Integer proxyPort,
            @NotNull
//            @DefaultValue("true") FIXME
            Boolean useSystem
    ) {
    }

}
