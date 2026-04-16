@file:Suppress("NonAsciiCharacters")

package movie.repository.jdbc

import movie.database.DatabaseConnectionFactory
import movie.database.DatabaseInitializer
import movie.model.seat.Seat
import movie.model.seat.SeatGrade
import movie.model.seat.SeatNumber
import movie.model.seat.Seats
import movie.repository.inmemory.InMemoryScreenRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.use

class JdbcReservedSeatRepositoryTest {
    private val isLocal = false
    private val movieRepository = JdbcMovieRepository(isLocal)
    private val screeningRepository =
        JdbcScreeningRepository(
            isLocal = isLocal,
            screenRepository = InMemoryScreenRepository,
            movieRepository = movieRepository,
        )
    private val reservedSeatRepository = JdbcReservedSeatRepository(isLocal)

    private fun dropTables() {
        val connection = DatabaseConnectionFactory.createConnection(isLocal)

        connection.use { conn ->
            conn.createStatement().use { statement ->
                statement.execute("SET REFERENTIAL_INTEGRITY FALSE")
                statement.execute("DROP TABLE IF EXISTS movie")
                statement.execute("DROP TABLE IF EXISTS screening")
                statement.execute("DROP TABLE IF EXISTS reservation")
                statement.execute("DROP TABLE IF EXISTS reserved_seat")
                statement.execute("SET REFERENTIAL_INTEGRITY TRUE")
            }
        }
    }

    @BeforeEach
    fun setUp() {
        dropTables()
        DatabaseInitializer.initTables(isLocal)
    }

    @Test
    fun `특정 상영 회차에 예약된 좌석을 저장하고 조회한다`() {
        // given
        val movie = requireNotNull(movieRepository.findByTitle("토이 스토리")) { "영화가 없습니다." }
        val date = LocalDate.of(2025, 9, 20)
        val screenings = screeningRepository.findBy(movie, date).toList()
        val screening = screenings[0]
        val seats =
            Seats(
                listOf(
                    Seat(SeatNumber('A', 1), SeatGrade.B),
                    Seat(SeatNumber('A', 2), SeatGrade.B),
                ),
            )

        // when
        reservedSeatRepository.save(screening, seats)
        val savedSeats = reservedSeatRepository.findSeatsByScreening(screening)

        // then
        assertThat(savedSeats.seatCount).isEqualTo(2)
        assertThat(savedSeats.seatNumbers).containsExactly(
            SeatNumber('A', 1),
            SeatNumber('A', 2),
        )
    }
}
