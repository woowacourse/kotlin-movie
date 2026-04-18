package movie.infrastructure.web

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.client.RestTestClient
import java.sql.Connection

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ReservationApiControllerTest(
    @param:LocalServerPort private val port: Int,
) {
    @Autowired
    private lateinit var connection: Connection

    private lateinit var client: RestTestClient

    @BeforeEach
    fun setUp() {
        resetReservationData()
        client =
            RestTestClient
                .bindToServer()
                .baseUrl("http://localhost:$port")
                .build()
    }

    @Test
    fun `예매 생성 시 201 Created를 반환한다`() {
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(validReservationRequest())
            .exchange()
            .expectStatus()
            .isCreated()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
    }

    @Test
    fun `예매 응답에 reservationId와 totalPrice가 포함된다`() {
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(validReservationRequest())
            .exchange()
            .expectStatus()
            .isCreated()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.reservationId")
            .exists()
            .jsonPath("$.reservations")
            .isArray()
            .jsonPath("$.reservations[0].screeningId")
            .exists()
            .jsonPath("$.reservations[0].seats[0]")
            .exists()
            .jsonPath("$.usedPoints")
            .exists()
            .jsonPath("$.paymentMethod")
            .exists()
            .jsonPath("$.totalPrice")
            .exists()
    }

    private fun validReservationRequest(): String =
        """
        {
          "reservations": [
            {
              "screeningId": 101,
              "seats": ["C2", "C3"]
            },
            {
              "screeningId": 202,
              "seats": ["E2"]
            },
            {
              "screeningId": 104,
              "seats": ["A1"]
            }
          ]
          ,"usedPoints": 2000
          ,"paymentMethod": "CREDIT_CARD"
        }
        """.trimIndent()

    private fun resetReservationData() {
        connection.createStatement().use { statement ->
            statement.executeUpdate("DELETE FROM reservation_seat")
            statement.executeUpdate("DELETE FROM reservation_item")
            statement.executeUpdate("DELETE FROM reservation")
            statement.executeUpdate("DELETE FROM reserved_seat")

            statement.executeUpdate("INSERT INTO reserved_seat (screening_id, seat_row, seat_column) VALUES (102, 'B', 2)")
            statement.executeUpdate("INSERT INTO reserved_seat (screening_id, seat_row, seat_column) VALUES (102, 'B', 3)")
            statement.executeUpdate("INSERT INTO reserved_seat (screening_id, seat_row, seat_column) VALUES (102, 'C', 3)")
            statement.executeUpdate("INSERT INTO reserved_seat (screening_id, seat_row, seat_column) VALUES (102, 'E', 4)")
        }
    }
}
