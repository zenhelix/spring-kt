package io.github.zenhelix.spring.autoconfiguration.web.client.properties.reactor.netty;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import io.github.zenhelix.spring.autoconfiguration.web.client.properties.AbstractHttpClientProperties;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.boot.convert.DurationUnit;

public class NettyReactorHttpClientProperties extends AbstractHttpClientProperties {

    private Boolean compress;
    private Boolean disableRetry;
    private Boolean followRedirect;
    private Boolean keepAlive;
    @DurationMin(millis = 0)
    private Duration writeTimeout;
    private Boolean ignoreSsl;

    public NettyReactorHttpClientProperties() {
        super();
        compress = null;
        disableRetry = null;
        followRedirect = null;
        keepAlive = null;
        writeTimeout = null;
        ignoreSsl = null;
    }

    @ConstructorBinding
    public NettyReactorHttpClientProperties(
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

            Boolean compress,
            Boolean disableRetry,
            Boolean followRedirect,
            Boolean keepAlive,
            @DurationUnit(ChronoUnit.SECONDS)
            Duration writeTimeout,
            Boolean ignoreSsl
    ) {
        super(baseUrl, connectTimeout, readTimeout, useProxy, proxy);
        this.compress = compress;
        this.disableRetry = disableRetry;
        this.followRedirect = followRedirect;
        this.keepAlive = keepAlive;
        this.writeTimeout = writeTimeout;
        this.ignoreSsl = ignoreSsl;
    }

    public Boolean getCompress() {
        return compress;
    }

    public void setCompress(Boolean compress) {
        this.compress = compress;
    }

    public Boolean getDisableRetry() {
        return disableRetry;
    }

    public void setDisableRetry(Boolean disableRetry) {
        this.disableRetry = disableRetry;
    }

    public Boolean getFollowRedirect() {
        return followRedirect;
    }

    public void setFollowRedirect(Boolean followRedirect) {
        this.followRedirect = followRedirect;
    }

    public Boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public Duration getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(Duration writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public Boolean getIgnoreSsl() {
        return ignoreSsl;
    }

    public void setIgnoreSsl(Boolean ignoreSsl) {
        this.ignoreSsl = ignoreSsl;
    }

}
