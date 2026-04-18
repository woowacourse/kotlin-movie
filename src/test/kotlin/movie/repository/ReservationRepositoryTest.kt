package movie.repository

import movie.domain.Movie
import movie.domain.MovieTitle
import movie.domain.Reservation
import movie.domain.Schedule
import movie.domain.seat.SeatNumber
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDateTime

class ReservationRepositoryTest {
    private lateinit var connection: Connection
    private lateinit var reservationRepository: ReservationRepository

    @BeforeEach
    fun setUp() {
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=MySQL", "sa", "")
        
        val schemaSql = this::class.java.classLoader.getResource("movie.sql")?.readText()
            ?: throw IllegalStateException("movie.sql 파일을 찾을 수 없습니다.")
        
        connection.createStatement().use { stmt ->
            schemaSql.split(";").filter { it.isNotBlank() }.forEach { sql ->
                stmt.execute(sql)
            }
            
            stmt.execute("INSERT INTO movie (id, title, running_time) VALUES (1, '테스트 영화', 120)")
            stmt.execute("INSERT INTO schedule (id, movie_id, start_time, end_time) VALUES (1, 1, '2026-04-17 10:00:00', '2026-04-17 12:00:00')")
        }
        
        reservationRepository = ReservationRepository(connection)
    }

    @AfterEach
    fun tearDown() {
        connection.createStatement().use { stmt ->
            stmt.execute("DROP ALL OBJECTS")
        }
        connection.close()
    }

    @Test
    fun `예약 정보를 저장하면 데이터베이스에서 확인할 수 있다`() {
        val movie = Movie(MovieTitle("테스트 영화"), 120)
        val schedule = Schedule(
            id = 1L,
            movie = movie,
            startTime = LocalDateTime.of(2026, 4, 17, 10, 0),
            endTime = LocalDateTime.of(2026, 4, 17, 12, 0)
        )
        val seats = listOf(SeatNumber("A1"), SeatNumber("A2"))
        val reservation = Reservation(schedule, seats)

        reservationRepository.save(reservation)

        connection.prepareStatement("SELECT COUNT(*) FROM reservation WHERE schedule_id = ?").use { pstmt ->
            pstmt.setLong(1, 1L)
            val rs = pstmt.executeQuery()
            rs.next()
            assertThat(rs.getInt(1)).isEqualTo(1)
        }

        connection.prepareStatement("SELECT seat_number FROM reserved_seat WHERE schedule_id = ?").use { pstmt ->
            pstmt.setLong(1, 1L)
            val rs = pstmt.executeQuery()
            val savedSeats = mutableListOf<String>()
            while (rs.next()) {
                savedSeats.add(rs.getString("seat_number"))
            }
            assertThat(savedSeats).containsExactlyInAnyOrder("A1", "A2")
        }
    }
}
