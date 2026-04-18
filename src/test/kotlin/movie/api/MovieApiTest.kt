package movie.api

import movie.MovieApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType

@SpringBootTest(classes = [MovieApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieApiTest {
    @LocalServerPort
    private var port: Int = 0

    private lateinit var client: RestTestClient

    @BeforeEach
    fun setUp() {
        movie.database.DatabaseFactory.getConnection().use { connection ->
            connection.createStatement().use { stmt ->
                stmt.execute("DELETE FROM reserved_seat")
                stmt.execute("DELETE FROM reservation")
            }
        }

        client = RestTestClient.bindToServer()
            .baseUrl("http://localhost:$port")
            .build()
    }

    @Test
    fun `영화 목록을 조회한다`() {
        client.get().uri("/api/movies")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.movies").isArray()
            .jsonPath("$.movies[0].title").exists()
            .jsonPath("$.movies[0].screenings").isArray()
    }

    @Test
    fun `예매를 생성한다`() {
        client.post().uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                  "reservations": [
                    {
                      "screeningId": 1,
                      "seats": ["A1", "A2"]
                    }
                  ],
                  "usedPoints": 0,
                  "paymentMethod": "CREDIT_CARD"
                }
                """.trimIndent()
            )
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.reservationId").exists()
            .jsonPath("$.totalPrice").exists()
    }

    @Test
    fun `이미 예약된 좌석을 예매하려는 경우 400 에러를 반환한다`() {
        val requestBody = """
                {
                  "reservations": [
                    {
                      "screeningId": 2,
                      "seats": ["B1"]
                    }
                  ],
                  "usedPoints": 0,
                  "paymentMethod": "CREDIT_CARD"
                }
                """.trimIndent()

        // First reservation
        client.post().uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody)
            .exchange()
            .expectStatus().isCreated()

        // Second reservation for same seat
        client.post().uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.error").isEqualTo("이미 예약된 좌석입니다.")
    }

    @Test
    fun `존재하지 않는 상영에 대해 예매를 요청하면 400 에러를 반환한다`() {
        client.post().uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                  "reservations": [
                    {
                      "screeningId": 9999,
                      "seats": ["A1"]
                    }
                  ],
                  "usedPoints": 0,
                  "paymentMethod": "CREDIT_CARD"
                }
                """.trimIndent()
            )
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.error").isEqualTo("존재하지 않는 상영 정보입니다.")
    }

    @Test
    fun `잘못된 요청 형식에 대해 400 에러를 반환한다`() {
        client.post().uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                  "invalid": "format"
                }
                """.trimIndent()
            )
            .exchange()
            .expectStatus().isBadRequest()
    }
}
