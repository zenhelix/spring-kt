package io.github.zenhelix.spring.web.client

import io.github.zenhelix.spring.web.FilePart
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClient

//Content-Type Header Extensions

//Content-Type Header APPLICATION/JSON Extensions
public fun RestClient.RequestBodySpec.contentJson(): RestClient.RequestBodySpec = contentType(MediaType.APPLICATION_JSON)
public fun RestClient.RequestBodySpec.contentJson(body: Any?): RestClient.RequestBodySpec = apply { body?.also { contentJson().body(it) } }

//Content-Type Header APPLICATION/XML Extensions
public fun RestClient.RequestBodySpec.contentXml(): RestClient.RequestBodySpec = contentType(MediaType.APPLICATION_XML)
public fun RestClient.RequestBodySpec.contentXml(body: Any?): RestClient.RequestBodySpec = apply { body?.also { contentXml().body(it) } }

//Content-Type Header MULTIPART/FORM-DATA Extensions

public fun RestClient.RequestBodySpec.contentMultipartFormData(): RestClient.RequestBodySpec = contentType(MediaType.MULTIPART_FORM_DATA)
public fun RestClient.RequestBodySpec.contentMultipartFormData(partName: String, file: FilePart): RestClient.RequestBodySpec =
    contentMultipartFormData { partFile(partName, file) }

public fun RestClient.RequestBodySpec.contentMultipartFormData(body: MultiValueMap<String, HttpEntity<*>>): RestClient.RequestBodySpec =
    contentMultipartFormData().body(body)

public fun RestClient.RequestBodySpec.contentMultipartFormData(initializer: MultipartBodyBuilder.() -> Unit = {}): RestClient.RequestBodySpec =
    contentMultipartFormData(multipart(initializer))


//Content-Type Header APPLICATION/X-WWW-FORM-URLENCODED Extensions
public fun RestClient.RequestBodySpec.contentFormUrlencoded(): RestClient.RequestBodySpec = contentType(MediaType.APPLICATION_FORM_URLENCODED)
public fun RestClient.RequestBodySpec.contentFormUrlencoded(name: String, value: String): RestClient.RequestBodySpec =
    contentFormUrlencoded().body(FormInserterBuilder().add(name, value).build())

public fun RestClient.RequestBodySpec.contentFormUrlencoded(formData: MultiValueMap<String, String>): RestClient.RequestBodySpec =
    contentFormUrlencoded().body(formData)

public fun RestClient.RequestBodySpec.contentFormUrlencoded(builder: FormInserterBuilder.() -> Unit = {}): RestClient.RequestBodySpec =
    contentFormUrlencoded().body(FormInserterBuilder().apply(builder).build())
