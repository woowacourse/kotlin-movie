package movie.infrastructure.web

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.client.RestTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MovieApiControllerTest(
    @param:LocalServerPort private val port: Int,
) {
    private lateinit var client: RestTestClient

    @BeforeEach
    fun setUp() {
        client =
            RestTestClient
                .bindToServer()
                .baseUrl("http://localhost:$port")
                .build()
    }

    @Test
    fun `영화 목록 조회 요청 시 200 OK를 반환한다`() {
        client
            .get()
            .uri("/api/movies")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
    }

    @Test
    fun `영화 목록 조회 응답에 영화와 상영 정보가 올바르게 포함된다`() {
        client
            .get()
            .uri("/api/movies")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.movies")
            .isArray()
            .jsonPath("$.movies[0].movieId")
            .exists()
            .jsonPath("$.movies[0].title")
            .exists()
            .jsonPath("$.movies[0].runningTimeMinutes")
            .exists()
            .jsonPath("$.movies[0].screenings")
            .isArray()
            .jsonPath("$.movies[0].screenings[0].screeningId")
            .exists()
            .jsonPath("$.movies[0].screenings[0].date")
            .exists()
            .jsonPath("$.movies[0].screenings[0].startTime")
            .exists()
            .jsonPath("$.movies[0].screenings[0].endTime")
            .exists()
    }
}
