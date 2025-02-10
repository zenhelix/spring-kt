package io.github.zenhelix.spring.webflux.client

import org.springframework.http.HttpMethod
import org.springframework.web.reactive.function.client.WebClient

public fun WebClient.deleteWithBody(): WebClient.RequestBodyUriSpec = method(HttpMethod.DELETE)