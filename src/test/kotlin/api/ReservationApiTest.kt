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
@TestPropertySource(properties = ["db.url=jdbc:h2:mem:reservationtest;DB_CLOSE_DELAY=-1"])
class ReservationApiTest(
    @param:LocalServerPort private val port: Int,
) {
    private lateinit var client: RestTestClient

    @BeforeEach
    fun setUp() {
        client = RestTestClient.bindToServer().baseUrl("http://localhost:$port").build()
    }

    @Test
    fun `예매 생성 요청 시 201 Created를 반환한다`() {
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                  "reservations": [{ "screeningId": 1, "seats": ["C2", "C3"] }],
                  "usedPoints": 0,
                  "paymentMethod": "CREDIT_CARD"
                }
                """.trimIndent(),
            ).exchange()
            .expectStatus()
            .isCreated
            .expectBody()
            .jsonPath("$.reservationId")
            .exists()
            .jsonPath("$.totalPrice")
            .exists()
    }

    @Test
    fun `예매 생성 응답에 결제 금액이 올바르게 포함된다`() {
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                  "reservations": [{ "screeningId": 2, "seats": ["E1"] }],
                  "usedPoints": 0,
                  "paymentMethod": "CASH"
                }
                """.trimIndent(),
            ).exchange()
            .expectStatus()
            .isCreated
            .expectBody()
            .jsonPath("$.totalPrice")
            .isEqualTo(14700)
    }

    @Test
    fun `존재하지 않는 상영 ID로 예매 요청 시 400을 반환한다`() {
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                  "reservations": [{ "screeningId": 999999, "seats": ["C1"] }],
                  "usedPoints": 0,
                  "paymentMethod": "CREDIT_CARD"
                }
                """.trimIndent(),
            ).exchange()
            .expectStatus()
            .isBadRequest
    }

    @Test
    fun `이미 예약된 좌석을 예매하면 400을 반환한다`() {
        val body =
            """
            {
              "reservations": [{ "screeningId": 3, "seats": ["C4"] }],
              "usedPoints": 0,
              "paymentMethod": "CREDIT_CARD"
            }
            """.trimIndent()

        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
            .exchange()
            .expectStatus()
            .isCreated

        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
            .exchange()
            .expectStatus()
            .isBadRequest
    }
}
