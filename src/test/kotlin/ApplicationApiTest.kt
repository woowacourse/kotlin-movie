import config.Application
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.client.RestTestClient
import persistence.CinemaDatabase
import java.util.UUID

@SpringBootTest(
    classes = [Application::class, ApiTestConfiguration::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ApplicationApiTest {
    @LocalServerPort
    private var port: Int = 0

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
    fun `영화 목록을 조회한다`() {
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
            .jsonPath("$.movies[0].id")
            .isEqualTo("movie-f1")
            .jsonPath("$.movies[0].title")
            .isEqualTo("F1 더 무비")
            .jsonPath("$.movies[0].runningTimeMinutes")
            .isEqualTo(130)
            .jsonPath("$.movies[0].screenings[0].id")
            .isEqualTo("screening-movie-f1-screen-1-2025-09-20T10:20")
            .jsonPath("$.movies[0].screenings[0].startAt")
            .isEqualTo("2025-09-20T10:20:00")
            .jsonPath("$.movies[0].screenings[0].endAt")
            .isEqualTo("2025-09-20T12:30:00")
    }

    @Test
    fun `예매를 생성한다`() {
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                  "reservations": [
                    {
                      "screeningId": "screening-movie-f1-screen-1-2025-09-20T10:20",
                      "seats": ["C2"]
                    }
                  ],
                  "usedPoints": 1000,
                  "paymentMethod": "CREDIT_CARD"
                }
                """.trimIndent(),
            ).exchange()
            .expectStatus()
            .isCreated()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.reservationId")
            .exists()
            .jsonPath("$.reservations[0].screeningId")
            .isEqualTo("screening-movie-f1-screen-1-2025-09-20T10:20")
            .jsonPath("$.reservations[0].seats[0]")
            .isEqualTo("C2")
            .jsonPath("$.usedPoints")
            .isEqualTo(1000)
            .jsonPath("$.paymentMethod")
            .isEqualTo("CREDIT_CARD")
            .jsonPath("$.totalPrice")
            .isEqualTo(12540)
    }

    @Test
    fun `이미 예약된 좌석을 예매하면 충돌 응답을 반환한다`() {
        val request =
            """
            {
              "reservations": [
                {
                  "screeningId": "screening-movie-f1-screen-1-2025-09-20T10:20",
                  "seats": ["A1"]
                }
              ],
              "usedPoints": 0,
              "paymentMethod": "CASH"
            }
            """.trimIndent()

        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .exchange()
            .expectStatus()
            .isCreated()

        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .exchange()
            .expectStatus()
            .isEqualTo(409)
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.message")
            .isEqualTo("해당 좌석은 이미 예약되었습니다.")
    }

    @Test
    fun `같은 사용자의 포인트는 요청 간에 누적 차감된다`() {
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                  "reservations": [
                    {
                      "screeningId": "screening-movie-f1-screen-1-2025-09-20T10:20",
                      "seats": ["A2"]
                    }
                  ],
                  "usedPoints": 1500,
                  "paymentMethod": "CASH"
                }
                """.trimIndent(),
            ).exchange()
            .expectStatus()
            .isCreated()

        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                  "reservations": [
                    {
                      "screeningId": "screening-movie-f1-screen-1-2025-09-20T13:00",
                      "seats": ["A3"]
                    }
                  ],
                  "usedPoints": 600,
                  "paymentMethod": "CASH"
                }
                """.trimIndent(),
            ).exchange()
            .expectStatus()
            .isBadRequest()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.message")
            .isEqualTo("차감액은 전체 포인트보다 작아야 합니다.")
    }

    @Test
    fun `존재하지 않는 상영에 대해 예매를 요청하면 찾을 수 없음 응답을 반환한다`() {
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                  "reservations": [
                    {
                      "screeningId": "screening-missing",
                      "seats": ["A1"]
                    }
                  ],
                  "usedPoints": 0,
                  "paymentMethod": "CASH"
                }
                """.trimIndent(),
            ).exchange()
            .expectStatus()
            .isNotFound()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.message")
            .isEqualTo("존재하지 않는 상영입니다.")
    }

    @Test
    fun `잘못된 요청 형식이면 잘못된 요청 응답을 반환한다`() {
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                  "reservations": "invalid"
                }
                """.trimIndent(),
            ).exchange()
            .expectStatus()
            .isBadRequest()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.message")
            .isEqualTo("입력된 값이 유효하지 않습니다.")
    }
}

@TestConfiguration
class ApiTestConfiguration {
    @Bean
    @Primary
    fun testCinemaDatabase(): CinemaDatabase = CinemaDatabase.inMemory(UUID.randomUUID().toString())
}
