package movie.api

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.assertj.core.api.Assertions.assertThat

class RestTestClient(private val baseUrl: String) {
    private val restTemplate = RestTemplate()

    fun get() = UriSpec(baseUrl, restTemplate, HttpMethod.GET)
    fun post() = UriBodySpec(baseUrl, restTemplate, HttpMethod.POST)

    class UriSpec(
        private val baseUrl: String,
        private val restTemplate: RestTemplate,
        private val method: HttpMethod
    ) {
        fun uri(uri: String): RequestHeadersSpec {
            return RequestHeadersSpec(baseUrl, restTemplate, method, uri)
        }
    }

    class RequestHeadersSpec(
        private val baseUrl: String,
        private val restTemplate: RestTemplate,
        private val method: HttpMethod,
        private val uri: String
    ) {
        private val headers = HttpHeaders()

        fun accept(mediaType: MediaType): RequestHeadersSpec {
            headers.accept = listOf(mediaType)
            return this
        }

        fun exchange(): ResponseSpec {
            val entity = HttpEntity<Any>(headers)
            return try {
                val response = restTemplate.exchange(baseUrl + uri, method, entity, String::class.java)
                ResponseSpec(response)
            } catch (e: HttpClientErrorException) {
                ResponseSpec(ResponseEntity.status(e.statusCode).headers(e.responseHeaders).body(e.responseBodyAsString))
            } catch (e: HttpServerErrorException) {
                ResponseSpec(ResponseEntity.status(e.statusCode).headers(e.responseHeaders).body(e.responseBodyAsString))
            }
        }
    }

    class UriBodySpec(
        private val baseUrl: String,
        private val restTemplate: RestTemplate,
        private val method: HttpMethod
    ) {
        fun uri(uri: String): RequestBodySpec {
            return RequestBodySpec(baseUrl, restTemplate, method, uri)
        }
    }

    class RequestBodySpec(
        private val baseUrl: String,
        private val restTemplate: RestTemplate,
        private val method: HttpMethod,
        private val uri: String
    ) {
        private val headers = HttpHeaders()
        private var body: Any? = null

        fun contentType(mediaType: MediaType): RequestBodySpec {
            headers.contentType = mediaType
            return this
        }

        fun body(body: Any): RequestBodySpec {
            this.body = body
            return this
        }

        fun exchange(): ResponseSpec {
            val entity = HttpEntity(body, headers)
            return try {
                val response = restTemplate.exchange(baseUrl + uri, method, entity, String::class.java)
                ResponseSpec(response)
            } catch (e: HttpClientErrorException) {
                ResponseSpec(ResponseEntity.status(e.statusCode).headers(e.responseHeaders).body(e.responseBodyAsString))
            } catch (e: HttpServerErrorException) {
                ResponseSpec(ResponseEntity.status(e.statusCode).headers(e.responseHeaders).body(e.responseBodyAsString))
            }
        }
    }

    class ResponseSpec(private val response: ResponseEntity<String>) {
        fun expectStatus(): StatusSpec = StatusSpec(this, response)
        fun expectHeader(): HeaderSpec = HeaderSpec(this, response)
        fun expectBody(): BodySpec = BodySpec(this, response)
    }

    class StatusSpec(private val responseSpec: ResponseSpec, private val response: ResponseEntity<String>) {
        fun isOk(): ResponseSpec {
            assertThat(response.statusCode.value())
                .withFailMessage("Expected 200 but was ${response.statusCode.value()}. Body: ${response.body}")
                .isEqualTo(200)
            return responseSpec
        }
        fun isCreated(): ResponseSpec {
            assertThat(response.statusCode.value())
                .withFailMessage("Expected 201 but was ${response.statusCode.value()}. Body: ${response.body}")
                .isEqualTo(201)
            return responseSpec
        }
        fun isBadRequest(): ResponseSpec {
            assertThat(response.statusCode.value())
                .withFailMessage("Expected 400 but was ${response.statusCode.value()}. Body: ${response.body}")
                .isEqualTo(400)
            return responseSpec
        }
    }

    class HeaderSpec(private val responseSpec: ResponseSpec, private val response: ResponseEntity<String>) {
        fun contentTypeCompatibleWith(mediaType: MediaType): ResponseSpec {
            assertThat(response.headers.contentType?.isCompatibleWith(mediaType)).isTrue()
            return responseSpec
        }
    }

    class BodySpec(private val responseSpec: ResponseSpec, private val response: ResponseEntity<String>) {
        private val jsonContext = com.jayway.jsonpath.JsonPath.parse(response.body ?: "{}")

        fun jsonPath(path: String): JsonPathSpec {
            return JsonPathSpec(this, jsonContext, path)
        }
    }

    class JsonPathSpec(private val bodySpec: BodySpec, private val context: com.jayway.jsonpath.DocumentContext, private val path: String) {
        fun isArray(): JsonPathSpec {
            val value: Any = context.read(path)
            assertThat(value).isInstanceOf(List::class.java)
            return this
        }

        fun exists(): JsonPathSpec {
            val value: Any? = context.read(path)
            assertThat(value).isNotNull
            return this
        }

        fun isEqualTo(expected: Any): JsonPathSpec {
            val value: Any = context.read(path)
            assertThat(value.toString()).isEqualTo(expected.toString())
            return this
        }

        fun jsonPath(newPath: String): JsonPathSpec {
            return bodySpec.jsonPath(newPath)
        }
    }

    companion object {
        fun bindToServer() = Builder()
    }

    class Builder {
        private var baseUrl: String = ""

        fun baseUrl(baseUrl: String): Builder {
            this.baseUrl = baseUrl
            return this
        }

        fun build(): RestTestClient {
            return RestTestClient(baseUrl)
        }
    }
}
