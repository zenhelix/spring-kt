package io.github.zenhelix.spring.web.client

import org.springframework.http.HttpMethod
import org.springframework.web.client.RestClient

public fun RestClient.deleteWithBody(): RestClient.RequestBodyUriSpec = method(HttpMethod.DELETE)