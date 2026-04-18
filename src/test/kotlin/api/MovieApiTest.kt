package api

import application.MovieApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.client.RestTestClient

@SpringBootTest(
    classes = [MovieApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@TestPropertySource(properties = ["db.url=jdbc:h2:mem:movietest;DB_CLOSE_DELAY=-1"])
class MovieApiTest(
    @param:LocalServerPort private val port: Int,
) {
    private lateinit var client: RestTestClient

    @BeforeEach
    fun setUp() {
        client = RestTestClient.bindToServer().baseUrl("http://localhost:$port").build()
    }

    @Test
    fun `영화 목록 조회 시 200 OK를 반환한다`() {
        client
            .get()
            .uri("/api/movies")
            .exchange()
            .expectStatus()
            .isOk
    }

    @Test
    fun `영화 목록 응답에 영화와 상영 정보가 포함된다`() {
        client
            .get()
            .uri("/api/movies")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectBody()
            .jsonPath("$.movies")
            .isArray()
            .jsonPath("$.movies[0].id")
            .exists()
            .jsonPath("$.movies[0].title")
            .exists()
            .jsonPath("$.movies[0].runningTimeMinutes")
            .exists()
            .jsonPath("$.movies[0].screenings")
            .isArray()
            .jsonPath("$.movies[0].screenings[0].id")
            .exists()
            .jsonPath("$.movies[0].screenings[0].startAt")
            .exists()
            .jsonPath("$.movies[0].screenings[0].endAt")
            .exists()
    }
}
