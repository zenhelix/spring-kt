package io.github.zenhelix.spring.http

import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity

public fun <B : ResponseEntity.HeadersBuilder<B>> ResponseEntity.HeadersBuilder<B>.contentDisposition(contentDisposition: ContentDisposition): B = header(
    HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString()
)
