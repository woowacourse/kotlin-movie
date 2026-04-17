package api

import api.dto.ReservationItemRequest
import api.dto.ReservationRequest
import db.DatabaseConfig
import model.discount.PaymentMethod
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.client.RestTestClient
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

class TestDatabaseInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        DatabaseConfig.configure("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1")
    }
}

@SpringBootTest(classes = [Application::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [TestDatabaseInitializer::class])
class MovieApiTest(
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
        clearTables()
        insertTestData()
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
            .jsonPath("$.movies[0].title")
            .isEqualTo("탑건: 매버릭")
            .jsonPath("$.movies[0].runningTimeMinutes")
            .isEqualTo(130)
            .jsonPath("$.movies[0].screenings[0].startAt")
            .exists()
            .jsonPath("$.movies[0].screenings[0].endAt")
            .exists()
    }

    @Test
    fun `예매 생성 요청 시 201 Created를 반환한다`() {
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                ReservationRequest(
                    reservations = listOf(ReservationItemRequest(getFirstScreeningId(), listOf("A1"))),
                    usedPoints = 0,
                    paymentMethod = PaymentMethod.CREDIT_CARD,
                ),
            ).exchange()
            .expectStatus()
            .isCreated()
    }

    @Test
    fun `예매 생성 응답에 reservationId와 totalPrice가 포함된다`() {
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(reservationBody(getFirstScreeningId(), listOf("A1", "A2")))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody()
            .jsonPath("$.reservationId")
            .exists()
            .jsonPath("$.totalPrice")
            .exists()
    }

    @Test
    fun `이미 예약된 좌석을 예매하려는 경우 409를 반환한다`() {
        val screeningId = getFirstScreeningId()

        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(reservationBody(screeningId, listOf("B1")))
            .exchange()
            .expectStatus()
            .isCreated()

        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(reservationBody(screeningId, listOf("B1")))
            .exchange()
            .expectStatus()
            .isEqualTo(409)
    }

    @Test
    fun `존재하지 않는 상영 ID로 예매 요청 시 404를 반환한다`() {
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(reservationBody(9999L, listOf("A1")))
            .exchange()
            .expectStatus()
            .isNotFound()
    }

    @Test
    fun `잘못된 JSON 형식으로 요청 시 400을 반환한다`() {
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body("invalid json")
            .exchange()
            .expectStatus()
            .isBadRequest()
    }

    private fun reservationBody(
        screeningId: Long,
        seats: List<String>,
    ) = ReservationRequest(
        reservations = listOf(ReservationItemRequest(screeningId, seats)),
        usedPoints = 0,
        paymentMethod = PaymentMethod.CREDIT_CARD,
    )

    private fun getFirstScreeningId(): Long {
        DatabaseConfig.getConnection().use { conn ->
            val rs = conn.createStatement().executeQuery("SELECT id FROM SCREENING LIMIT 1")
            if (rs.next()) return rs.getLong("id")
        }
        throw IllegalStateException("테스트용 상영 데이터가 없습니다")
    }

    private fun insertTestData() {
        DatabaseConfig.getConnection().use { conn ->
            conn
                .prepareStatement(
                    "INSERT INTO MOVIE (title, running_time, start_date, end_date) VALUES (?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS,
                ).use { stmt ->
                    stmt.setString(1, "탑건: 매버릭")
                    stmt.setInt(2, 130)
                    stmt.setDate(3, Date.valueOf(LocalDate.of(2026, 4, 1)))
                    stmt.setDate(4, Date.valueOf(LocalDate.of(2026, 4, 30)))
                    stmt.executeUpdate()
                    val movieId = stmt.generatedKeys.also { it.next() }.getLong(1)

                    conn
                        .prepareStatement(
                            "INSERT INTO SCREENING (movie_id, start_time, end_time) VALUES (?, ?, ?)",
                        ).use { screeningStmt ->
                            screeningStmt.setLong(1, movieId)
                            screeningStmt.setTimestamp(
                                2,
                                Timestamp.valueOf(LocalDateTime.of(2026, 4, 17, 10, 0)),
                            )
                            screeningStmt.setTimestamp(
                                3,
                                Timestamp.valueOf(LocalDateTime.of(2026, 4, 17, 12, 10)),
                            )
                            screeningStmt.executeUpdate()
                        }
                }
        }
    }

    private fun clearTables() {
        DatabaseConfig.getConnection().use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute("DELETE FROM RESERVATION_ITEM")
                stmt.execute("DELETE FROM RESERVATION")
                stmt.execute("DELETE FROM SCREENING")
                stmt.execute("DELETE FROM MOVIE")
            }
        }
    }
}
