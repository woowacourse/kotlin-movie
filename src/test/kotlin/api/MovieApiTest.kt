package api

import api.dto.ReservationItemDto
import api.dto.ReservationRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient

import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MovieApiControllerTest(
    @LocalServerPort private val port: Int,
) {
    init {
        System.setProperty("spring.profiles.active", "test")
    }

    private lateinit var client: RestTestClient

    @BeforeEach
    fun setUp() {
        client = RestTestClient.bindToServer()
            .baseUrl("http://localhost:$port")
            .build()

        repository.JdbcConnection.getConnection().use { connection ->
            connection.createStatement().use { stmt ->
                stmt.execute("SET REFERENTIAL_INTEGRITY FALSE")
                stmt.execute("TRUNCATE TABLE reserved_seats RESTART IDENTITY")
                stmt.execute("TRUNCATE TABLE reservations RESTART IDENTITY")
                stmt.execute("TRUNCATE TABLE screenings RESTART IDENTITY")
                stmt.execute("TRUNCATE TABLE screening_rooms RESTART IDENTITY")
                stmt.execute("TRUNCATE TABLE movies RESTART IDENTITY")
                stmt.execute("SET REFERENTIAL_INTEGRITY TRUE")

                stmt.execute("INSERT INTO movies (id, title, running_time, start_date, end_date) VALUES (1, '커브볼', 169, '2026-04-10', '2026-09-30')")
                stmt.execute("INSERT INTO movies (id, title, running_time, start_date, end_date) VALUES (2, '하로', 180, '2026-04-01', '2026-09-30')")
                stmt.execute("INSERT INTO screening_rooms (id, name, operating_start_time, operating_end_time) VALUES (1, '1관', '09:00:00', '23:00:00')")
                stmt.execute("INSERT INTO screening_rooms (id, name, operating_start_time, operating_end_time) VALUES (2, '2관', '09:00:00', '23:00:00')")


                stmt.execute("INSERT INTO screenings (id, movie_id, room_id, start_time) VALUES (101, 1, 1, '2026-04-10 09:30:00')")
                stmt.execute("INSERT INTO screenings (id, movie_id, room_id, start_time) VALUES (102, 1, 2, '2026-04-10 13:00:00')")
                stmt.execute("INSERT INTO screenings (id, movie_id, room_id, start_time) VALUES (201, 2, 2, '2026-04-20 13:00:00')")
                stmt.execute("INSERT INTO screenings (id, movie_id, room_id, start_time) VALUES (202, 2, 2, '2026-04-30 09:00:00')")
            }
        }
    }

    @Test
    fun `영화 목록 조회 요청 시 200 OK를 반환하고 영화와 상영 정보가 포함된다`() {
        client.get().uri("/api/movies")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody()

            .jsonPath("$.movies").isArray()

            .jsonPath("$.movies[0].id").exists()
            .jsonPath("$.movies[0].title").exists()
            .jsonPath("$.movies[0].runningTimeMinutes").isNumber()

            .jsonPath("$.movies[0].screenings").isArray()
            .jsonPath("$.movies[0].screenings[0].startAt").exists()
            .jsonPath("$.movies[0].screenings[0].endAt").exists()
    }

    @Test
    fun `정상적인 예매 요청 시 210 Created 응답과 함께 예매 내역을 반환한다`() {
        // given
        val request = ReservationRequest(
            reservations = listOf(
                ReservationItemDto(screeningId = 101, seats = listOf("C2", "C3"))
            ),
            usedPoints = 1000,
            paymentMethod = "CREDIT_CARD"
        )

        // when & then
        client.post().uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.reservationId").exists()
            .jsonPath("$.totalPrice").isNumber()
            .jsonPath("$.paymentMethod").isEqualTo("CREDIT_CARD")
            .jsonPath("$.reservations[0].screeningId").isEqualTo(101)
            .jsonPath("$.reservations[0].seats").isArray()
    }


    @Test
    fun `여러 영화를 한 번에 예매할 때 각 상영별 할인이 올바르게 적용되어 최종 금액이 계산된다`() {
        // given
        val request = mapOf(
            "reservations" to listOf(
                mapOf("screeningId" to 101, "seats" to listOf("C2", "C3")),
                mapOf("screeningId" to 102, "seats" to listOf("E2")),
                mapOf("screeningId" to 201, "seats" to listOf("A1"))
            ),
            "usedPoints" to 2000,
            "paymentMethod" to "CREDIT_CARD"
        )

        // when & then
        client.post().uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.totalPrice").isEqualTo(50065)
            .jsonPath("$.usedPoints").isEqualTo(2000)
            .jsonPath("$.reservationId").exists()
    }
}
