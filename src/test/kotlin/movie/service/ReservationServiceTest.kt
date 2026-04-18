package movie.service

import movie.domain.Movie
import movie.domain.MovieTitle
import movie.domain.Reservation
import movie.domain.Schedule
import movie.domain.seat.SeatNumber
import movie.repository.ReservationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDateTime

class ReservationServiceTest {
    private lateinit var connection: Connection
    private lateinit var reservationService: ReservationService

    @BeforeEach
    fun setUp() {
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=MySQL", "sa", "")
        
        val schemaSql = this::class.java.classLoader.getResource("movie.sql")?.readText()
            ?: throw IllegalStateException("movie.sql 파일을 찾을 수 없습니다.")
        
        connection.createStatement().use { stmt ->
            schemaSql.split(";").filter { it.isNotBlank() }.forEach { sql ->
                stmt.execute(sql)
            }
            
            stmt.execute("INSERT INTO movie (id, title, running_time) VALUES (1, '시동', 120)")
            stmt.execute("INSERT INTO schedule (id, movie_id, start_time, end_time) VALUES (1, 1, '2026-04-17 10:00:00', '2026-04-17 12:00:00')")
        }
        
        val reservationRepository = ReservationRepository(connection)
        reservationService = ReservationService(reservationRepository)
    }

    @AfterEach
    fun tearDown() {
        connection.createStatement().use { it.execute("DROP ALL OBJECTS") }
        connection.close()
    }

    @Test
    fun `서비스를 통해 예약을 저장하면 DB에 반영된다`() {
        val movie = Movie(MovieTitle("시동"), 120)
        val schedule = Schedule(
            id = 1L,
            movie = movie,
            startTime = LocalDateTime.of(2026, 4, 17, 10, 0),
            endTime = LocalDateTime.of(2026, 4, 17, 12, 0)
        )
        val seats = listOf(SeatNumber("C1"))
        val reservation = Reservation(schedule, seats)

        reservationService.saveReservation(reservation)

        connection.prepareStatement("SELECT COUNT(*) FROM reservation").use { pstmt ->
            val rs = pstmt.executeQuery()
            rs.next()
            assertThat(rs.getInt(1)).isEqualTo(1)
        }
    }
}
