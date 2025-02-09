package io.github.zenhelix.spring.webflux.client

import io.github.zenhelix.spring.web.FilePart
import io.github.zenhelix.spring.web.client.FormInserterBuilder
import io.github.zenhelix.spring.web.client.multipart
import io.github.zenhelix.spring.web.toSingleMultipart
import kotlinx.coroutines.flow.Flow
import org.reactivestreams.Publisher
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.body

//Content-Type Header Extensions

//Content-Type Header APPLICATION/JSON Extensions
public fun WebClient.RequestBodySpec.contentJson(): WebClient.RequestBodySpec = contentType(MediaType.APPLICATION_JSON)
public fun WebClient.RequestBodySpec.contentJson(body: Any?): WebClient.RequestBodySpec = apply { body?.also { contentJson().bodyValue(it) } }
public inline fun <reified T : Any, S : Publisher<T>> WebClient.RequestBodySpec.contentJson(publisher: S): WebClient.RequestHeadersSpec<*> =
    contentJson().body(publisher)

public inline fun <reified T : Any> WebClient.RequestBodySpec.contentJson(flow: Flow<T>): WebClient.RequestHeadersSpec<*> = contentJson().body(flow)

//Content-Type Header APPLICATION/XML Extensions
public fun WebClient.RequestBodySpec.contentXml(): WebClient.RequestBodySpec = contentType(MediaType.APPLICATION_XML)
public fun WebClient.RequestBodySpec.contentXml(body: Any?): WebClient.RequestBodySpec = apply { body?.also { contentXml().bodyValue(it) } }
public inline fun <reified T : Any, S : Publisher<T>> WebClient.RequestBodySpec.contentXml(publisher: S): WebClient.RequestHeadersSpec<*> =
    contentXml().body(publisher)

public inline fun <reified T : Any> WebClient.RequestBodySpec.contentXml(flow: Flow<T>): WebClient.RequestHeadersSpec<*> = contentXml().body(flow)

//Content-Type Header MULTIPART/FORM-DATA Extensions

public fun WebClient.RequestBodySpec.contentMultipartFormData(): WebClient.RequestBodySpec = contentType(MediaType.MULTIPART_FORM_DATA)
public fun WebClient.RequestBodySpec.contentMultipartFormData(partName: String, file: FilePart.FilePartBytes): WebClient.RequestHeadersSpec<*> =
    contentMultipartFormData(file.toSingleMultipart(partName))

public fun WebClient.RequestBodySpec.contentMultipartFormData(body: MultiValueMap<String, HttpEntity<*>>): WebClient.RequestHeadersSpec<*> =
    contentMultipartFormData().body(BodyInserters.fromMultipartData(body))

public fun WebClient.RequestBodySpec.contentMultipartFormData(initializer: MultipartBodyBuilder.() -> Unit = {}): WebClient.RequestHeadersSpec<*> =
    contentMultipartFormData(multipart(initializer))


//Content-Type Header APPLICATION/X-WWW-FORM-URLENCODED Extensions
public fun WebClient.RequestBodySpec.contentFormUrlencoded(): WebClient.RequestBodySpec = contentType(MediaType.APPLICATION_FORM_URLENCODED)
public fun WebClient.RequestBodySpec.contentFormUrlencoded(name: String, value: String): WebClient.RequestHeadersSpec<*> =
    contentFormUrlencoded().body(BodyInserters.fromFormData(name, value))

public fun WebClient.RequestBodySpec.contentFormUrlencoded(formData: MultiValueMap<String, String>): WebClient.RequestHeadersSpec<*> =
    contentFormUrlencoded().body(BodyInserters.fromFormData(formData))

public fun WebClient.RequestBodySpec.contentFormUrlencoded(builder: FormInserterBuilder.() -> Unit = {}): WebClient.RequestHeadersSpec<*> =
    contentFormUrlencoded().body(BodyInserters.fromFormData(FormInserterBuilder().apply(builder).build()))
