package io.github.zenhelix.spring.autoconfiguration.web.client.properties.apache;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import io.github.zenhelix.spring.autoconfiguration.web.client.properties.AbstractHttpClientProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.boot.convert.DurationUnit;

public class ApacheHttpAsyncClientProperties extends AbstractHttpClientProperties {

    private Boolean connectionManagerShared;
    @Valid
    private RequestConfigProperties request;
    @Valid
    private PoolProperties pool;

    public ApacheHttpAsyncClientProperties() {
        super();
        this.connectionManagerShared = null;
        this.request = null;
        this.pool = null;
    }

    @ConstructorBinding
    public ApacheHttpAsyncClientProperties(
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

            Boolean connectionManagerShared,
            RequestConfigProperties request,
            PoolProperties pool
    ) {
        super(baseUrl, connectTimeout, readTimeout, useProxy, proxy);

        this.connectionManagerShared = connectionManagerShared;
        this.request = request;
        this.pool = pool;
    }

    public Boolean getConnectionManagerShared() {
        return connectionManagerShared;
    }

    public void setConnectionManagerShared(Boolean connectionManagerShared) {
        this.connectionManagerShared = connectionManagerShared;
    }

    public RequestConfigProperties getRequest() {
        return request;
    }

    public void setRequest(RequestConfigProperties request) {
        this.request = request;
    }

    public PoolProperties getPool() {
        return pool;
    }

    public void setPool(PoolProperties pool) {
        this.pool = pool;
    }

    public record PoolProperties(
            @Positive
            Integer maxConnections,
            @Positive
            Integer maxConnectionsPerRoute,
            @Valid
            ConnectionConfigProperties connection
    ) {
    }

    public record RequestConfigProperties(
            @DurationMin(millis = 0)
            @DurationUnit(ChronoUnit.SECONDS)
            Duration connectionRequestTimeout,
            @DurationMin(millis = 0)
            @DurationUnit(ChronoUnit.SECONDS)
            Duration connectionKeepAlive,
            Boolean redirectsEnabled,
            Boolean circularRedirectsAllowed,
            @PositiveOrZero
            Integer maxRedirects,
            Boolean protocolUpgradeEnabled
    ) {
    }

    public record ConnectionConfigProperties(
            @DurationMin(millis = 0)
            @DurationUnit(ChronoUnit.SECONDS)
            Duration connectionTimeToLive,
            @DurationMin(millis = 0)
            @DurationUnit(ChronoUnit.SECONDS)
            Duration validateAfterInactivityTimeout,
            @DurationMin(millis = 0)
            @DurationUnit(ChronoUnit.SECONDS)
            Duration socketTimeout
    ) {
    }

}
