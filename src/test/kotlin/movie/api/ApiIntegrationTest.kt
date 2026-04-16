@file:Suppress("NonAsciiCharacters")

package movie.api

import movie.api.dto.ReservationRequest
import movie.api.dto.ScreeningReservationRequest
import movie.database.DatabaseInitializer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiIntegrationTest {
    @LocalServerPort
    private var port: Int = 0

    private lateinit var client: RestTestClient

    @BeforeEach
    fun setUp() {
        DatabaseInitializer.initTables(isLocal = false)
        client =
            RestTestClient
                .bindToServer()
                .baseUrl("http://localhost:$port")
                .build()
    }

    @Test
    fun `영화 목록 조회 API - 정상적으로 영화 목록을 조회하면 200 OK 상태 코드를 반환한다`() {
        client
            .get()
            .uri("/api/movies")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.movies")
            .isArray
            .jsonPath("$.movies[0].title")
            .exists()
            .jsonPath("$.movies[0].screenings")
            .isArray
            .jsonPath("$.movies[0].screenings[0].startAt")
            .exists()
            .jsonPath("$.movies[0].screenings[0].endAt")
            .exists()
    }

    @Test
    fun `예매 생성 API - 올바른 요청 데이터로 예매를 진행하면 201 Created 상태 코드를 반환한다`() {
        val request =
            ReservationRequest(
                reservations =
                    listOf(
                        ScreeningReservationRequest(
                            screeningId = 101L,
                            seats = listOf("A1", "A2"),
                        ),
                    ),
                usedPoints = 1000,
                paymentMethod = "CREDIT_CARD",
            )

        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody()
            .jsonPath("$.reservationId")
            .exists()
            .jsonPath("$.totalPrice")
            .exists()
    }

    @Test
    fun `예매 생성 API - 이미 예약이 완료된 좌석에 대해 예매를 요청하면 400 Bad Request를 반환한다`() {
        // 첫 예약
        val firstRequest =
            ReservationRequest(
                reservations =
                    listOf(
                        ScreeningReservationRequest(
                            screeningId = 101L,
                            seats = listOf("B1"),
                        ),
                    ),
                usedPoints = 0,
                paymentMethod = "CASH",
            )

        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(firstRequest)
            .exchange()
            .expectStatus()
            .isCreated

        // 겹치는 예약
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(firstRequest)
            .exchange()
            .expectStatus()
            .isBadRequest
    }

    @Test
    fun `예매 생성 API - 존재하지 않는 상영 회차로 예매를 요청하면 400 Bad Request를 반환한다`() {
        val request =
            ReservationRequest(
                reservations =
                    listOf(
                        ScreeningReservationRequest(
                            screeningId = 9999L,
                            seats = listOf("A1"),
                        ),
                    ),
                usedPoints = 0,
                paymentMethod = "CREDIT_CARD",
            )

        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .exchange()
            .expectStatus()
            .isBadRequest
    }

    @Test
    fun `예매 생성 API - 결제 방식이 잘못된 경우 400 Bad Request를 반환한다`() {
        val request =
            ReservationRequest(
                reservations =
                    listOf(
                        ScreeningReservationRequest(
                            screeningId = 101L,
                            seats = listOf("A3"),
                        ),
                    ),
                usedPoints = 0,
                paymentMethod = "INVALID_PAYMENT",
            )

        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .exchange()
            .expectStatus()
            .isBadRequest
    }
}
