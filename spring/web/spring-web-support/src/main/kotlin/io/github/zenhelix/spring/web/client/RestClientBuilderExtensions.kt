package io.github.zenhelix.spring.web.client

import org.springframework.web.client.RestClient.Builder

public fun Builder.optionalBaseUrl(baseUrl: String?): Builder = apply { builder -> baseUrl?.let { builder.baseUrl(it) } }
