package io.github.zenhelix.spring.web

import org.springframework.web.util.UriBuilder

public inline fun <reified T : Any> UriBuilder.queryParamIfPresent(name: String, value: T?): UriBuilder = apply { value?.also { this.queryParam(name, it) } }
public inline fun <reified T : Collection<*>> UriBuilder.queryParamIfPresent(name: String, value: T?): UriBuilder =
    apply { value?.also { this.queryParam(name, it) } }
