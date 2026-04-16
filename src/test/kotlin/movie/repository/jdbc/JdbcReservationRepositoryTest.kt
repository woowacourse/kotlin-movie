@file:Suppress("NonAsciiCharacters")

package movie.repository.jdbc

import movie.database.DatabaseConnectionFactory
import movie.database.DatabaseInitializer
import movie.model.reservation.Reservation
import movie.model.seat.Seat
import movie.model.seat.SeatGrade
import movie.model.seat.SeatNumber
import movie.model.seat.Seats
import movie.repository.inmemory.InMemoryScreenRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

class JdbcReservationRepositoryTest {
    private val isLocal = false
    private val movieRepository = JdbcMovieRepository(isLocal)
    private val screeningRepository =
        JdbcScreeningRepository(
            isLocal = isLocal,
            screenRepository = InMemoryScreenRepository,
            movieRepository = movieRepository,
        )
    private val reservationRepository = JdbcReservationRepository(isLocal, screeningRepository)

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
    fun `여러 예약을 저장하고 특정 날짜의 예약만 조회한다`() {
        // given
        val movie =
            requireNotNull(movieRepository.findByTitle("F1 더 무비")) {
                "'F1 더 무비' 영화가 없습니다."
            }
        val date1 = LocalDate.of(2025, 9, 20)

        val screening1 = screeningRepository.findBy(movie, date1).toList()[0]
        val seats1 = Seats(listOf(Seat(SeatNumber('A', 1), SeatGrade.B)))
        val reservation1 = Reservation(screening1, seats1)

        val screening2 = screeningRepository.findBy(movie, date1).toList()[1]
        val seats2 = Seats(listOf(Seat(SeatNumber('B', 1), SeatGrade.B)))
        val reservation2 = Reservation(screening2, seats2)

        // when
        reservationRepository.save(reservation1)
        reservationRepository.save(reservation2)

        val savedReservations = reservationRepository.findByDate(date1).toList()

        // then
        assertThat(savedReservations).hasSize(2)
        assertThat(savedReservations.map { it.screening.id }).containsExactlyInAnyOrder(
            screening1.id,
            screening2.id,
        )
    }
}
