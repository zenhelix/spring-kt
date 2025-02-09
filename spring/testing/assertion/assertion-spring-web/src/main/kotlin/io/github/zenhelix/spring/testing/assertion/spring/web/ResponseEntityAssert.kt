package io.github.zenhelix.spring.testing.assertion.spring.web

import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions
import org.assertj.core.api.ObjectAssert
import org.hamcrest.Matcher
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.json.JsonAssert
import org.springframework.test.json.JsonCompareMode
import org.springframework.test.util.JsonPathExpectationsHelper
import java.net.URI

public open class ResponseEntityAssert<E> protected constructor(actual: ResponseEntity<out E>) :
    AbstractAssert<ResponseEntityAssert<E>, ResponseEntity<out E>>(actual, ResponseEntityAssert::class.java) {

    public companion object {
        public fun <E> assertThat(actual: ResponseEntity<out E>): ResponseEntityAssert<E> = ResponseEntityAssert(actual)
    }

    public fun httpStatus(httpStatus: HttpStatus): ResponseEntityAssert<E> {
        ObjectAssert(actual.statusCode).isEqualTo(httpStatus)
        return this
    }

    public val ok: ResponseEntityAssert<E>
        get() = httpStatus(HttpStatus.OK)

    public val created: ResponseEntityAssert<E>
        get() = httpStatus(HttpStatus.CREATED)

    public val internalServerError: ResponseEntityAssert<E>
        get() = httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)

    public val notFound: ResponseEntityAssert<E>
        get() = httpStatus(HttpStatus.NOT_FOUND)

    public val badRequest: ResponseEntityAssert<E>
        get() = httpStatus(HttpStatus.BAD_REQUEST)

    public val locked: ResponseEntityAssert<E>
        get() = httpStatus(HttpStatus.LOCKED)

    public val moved: ResponseEntityAssert<E>
        get() = httpStatus(HttpStatus.MOVED_PERMANENTLY)

    public val unauthorized: ResponseEntityAssert<E>
        get() = httpStatus(HttpStatus.UNAUTHORIZED)

    public val forbidden: ResponseEntityAssert<E>
        get() = httpStatus(HttpStatus.FORBIDDEN)

    public val conflict: ResponseEntityAssert<E>
        get() = httpStatus(HttpStatus.CONFLICT)

    public fun headers(name: String, vararg values: String): ResponseEntityAssert<E> {
        Assertions.assertThat(actual.headers).containsEntry(name, values.asList())
        return this
    }

    public fun hasHeaders(vararg name: String): ResponseEntityAssert<E> = apply {
        Assertions.assertThat(actual.headers).containsKeys(*name)
    }

    public fun hasContentType(contentType: MediaType): ResponseEntityAssert<E> {
        Assertions.assertThat(actual.headers.contentType).isEqualTo(contentType)
        return this
    }

    public fun hasLocation(location: String): ResponseEntityAssert<E> {
        Assertions.assertThat(actual.headers.location).isEqualTo(URI(location))
        return this
    }

    public fun hasAccessControlAllowHeaders(vararg headers: String): ResponseEntityAssert<E> {
        Assertions.assertThat(actual.headers.accessControlAllowHeaders).containsExactlyInAnyOrder(*headers)
        return this
    }

    public fun hasAccessControlExposeHeaders(vararg headers: String): ResponseEntityAssert<E> {
        Assertions.assertThat(actual.headers.accessControlExposeHeaders).containsExactlyInAnyOrder(*headers)
        return this
    }

    public fun hasAccessControlMaxAge(maxAge: Long): ResponseEntityAssert<E> {
        Assertions.assertThat(actual.headers.accessControlMaxAge).isEqualTo(maxAge)
        return this
    }

    public fun hasAccessControlMethods(vararg methods: HttpMethod): ResponseEntityAssert<E> {
        Assertions.assertThat(actual.headers.accessControlAllowMethods).containsExactlyInAnyOrder(*methods)
        return this
    }

    public fun hasAccessControlAllowOrigin(origin: String): ResponseEntityAssert<E> {
        Assertions.assertThat(actual.headers.accessControlAllowOrigin).isEqualTo(origin)
        return this
    }

    public val contentTypeIsNull: ResponseEntityAssert<E>
        get() {
            Assertions.assertThat(actual.headers.contentType).isNull()
            return this
        }

    public val contentTypeIsJson: ResponseEntityAssert<E>
        get() = hasContentType(MediaType.APPLICATION_JSON)

    public val contentTypeIsProblemJson: ResponseEntityAssert<E>
        get() = hasContentType(MediaType.APPLICATION_PROBLEM_JSON)

    public fun bodyJson(json: String): ResponseEntityAssert<E> {
        JsonAssert.comparator(JsonCompareMode.LENIENT).assertIsMatch(json, actual.body as String)
        return this
    }

    public fun <T> bodyJsonPatch(expression: String, matcher: Matcher<T>): ResponseEntityAssert<E> {
        JsonPathExpectationsHelper(expression).assertValue(actual.body as String, matcher)
        return this
    }

    public open fun body(body: E): ResponseEntityAssert<E> {
        Assertions.assertThat(actual.body).isEqualTo(body)
        return this
    }

    public fun bodyIgnoringGivenFields(body: E, vararg propertiesOrFieldsToIgnore: String): ResponseEntityAssert<E> {
        Assertions.assertThat(actual.body).usingRecursiveComparison().ignoringFields(*propertiesOrFieldsToIgnore).isEqualTo(body)
        return this
    }

    public val bodyIsEmpty: ResponseEntityAssert<E>
        get() {
            Assertions.assertThat(actual.hasBody()).isFalse
            return this
        }

    public val bodyIsNotEmpty: ResponseEntityAssert<E>
        get() {
            Assertions.assertThat(actual.hasBody()).isTrue
            return this
        }

    public fun bodyAssert(invoke: (body: E?) -> Unit): ResponseEntityAssert<E> {
        invoke(actual.body)
        return this
    }

}
