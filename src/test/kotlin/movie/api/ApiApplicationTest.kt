package movie.api

import movie.data.db.DatabaseInitializer
import movie.data.db.DatabaseManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiApplicationTest(
    @param:LocalServerPort private val port: Int,
) {
    private lateinit var client: RestTestClient

    @BeforeEach
    fun setUp() {
        DatabaseManager.url = "jdbc:h2:mem:test-${System.nanoTime()};DB_CLOSE_DELAY=-1"
        DatabaseInitializer.initialize()

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
                      "screeningId": 1,
                      "seats": ["C2", "C3"]
                    }
                  ],
                  "usedPoints": 2000,
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
            .jsonPath("$.totalPrice")
            .exists()
    }

    @Test
    fun `영화 목록 조회 응답에 영화와 상영 정보가 포함된다`() {
        client
            .get()
            .uri("/api/movies")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
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

    @Test
    fun `예매 생성 응답에 예매 정보와 결제 금액이 포함된다`() {
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                  "reservations": [
                    { "screeningId": 1, "seats": ["D1"] }
                  ],
                  "usedPoints": 0,
                  "paymentMethod": "CREDIT_CARD"
                }
                """.trimIndent(),
            ).exchange()
            .expectStatus()
            .isCreated()
            .expectBody()
            .jsonPath("$.reservationId")
            .exists()
            .jsonPath("$.reservations[0].screeningId")
            .isEqualTo(1)
            .jsonPath("$.reservations[0].seats[0]")
            .isEqualTo("D1")
            .jsonPath("$.usedPoints")
            .isEqualTo(0)
            .jsonPath("$.paymentMethod")
            .isEqualTo("CREDIT_CARD")
            .jsonPath("$.totalPrice")
            .exists()
    }

    @Test
    fun `이미 예약된 좌석을 예매하면 오류 응답을 반환한다`() {
        // 먼저 예매
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                  "reservations": [
                    { "screeningId": 3, "seats": ["A1"] }
                  ],
                  "usedPoints": 0,
                  "paymentMethod": "CREDIT_CARD"
                }
                """.trimIndent(),
            ).exchange()
            .expectStatus()
            .isCreated()

        // 같은 좌석 다시 예매
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                  "reservations": [
                    { "screeningId": 3, "seats": ["A1"] }
                  ],
                  "usedPoints": 0,
                  "paymentMethod": "CREDIT_CARD"
                }
                """.trimIndent(),
            ).exchange()
            .expectStatus()
            .isBadRequest()
    }

    @Test
    fun `존재하지 않는 상영에 대해 예매하면 오류 응답을 반환한다`() {
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                  "reservations": [
                    { "screeningId": 999, "seats": ["A1"] }
                  ],
                  "usedPoints": 0,
                  "paymentMethod": "CREDIT_CARD"
                }
                """.trimIndent(),
            ).exchange()
            .expectStatus()
            .isBadRequest()
    }

    @Test
    fun `잘못된 결제 수단으로 요청하면 오류 응답을 반환한다`() {
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                  "reservations": [
                    { "screeningId": 1, "seats": ["B1"] }
                  ],
                  "usedPoints": 0,
                  "paymentMethod": "BITCOIN"
                }
                """.trimIndent(),
            ).exchange()
            .expectStatus()
            .isBadRequest()
    }
}
