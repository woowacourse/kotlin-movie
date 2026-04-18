package db

import model.reservation.Reservation
import model.reservation.Reservations
import model.seat.SeatNumber
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDate
import java.util.UUID

class JdbcReservationRepositoryTest {
    private lateinit var connection: Connection
    private lateinit var movieRepository: JdbcMovieRepository
    private lateinit var screeningRepository: JdbcScreeningRepository
    private lateinit var repository: JdbcReservationRepository

    @BeforeEach
    fun setUp() {
        connection = DriverManager.getConnection("jdbc:h2:mem:${UUID.randomUUID()};DB_CLOSE_DELAY=-1", "sa", "")
        DataInitializer(connection).initialize()
        movieRepository = JdbcMovieRepository(connection)
        screeningRepository = JdbcScreeningRepository(connection)
        repository = JdbcReservationRepository(connection, movieRepository, screeningRepository)
    }

    @Test
    fun `예약을 저장하고 ID를 반환한다`() {
        val screening = loadFirstScreening()
        val seats = screening.reserve(listOf(SeatNumber('C', 2), SeatNumber('C', 3)))
        val reservations = Reservations().addReservation(Reservation(screening, seats))

        val reservationId = repository.save(reservations)

        assertThat(reservationId).isGreaterThan(0)
    }

    @Test
    fun `예약 저장 후 해당 좌석은 다시 로드 시 예약 불가 상태로 복원된다`() {
        val screening = loadFirstScreening()
        val seatNumber = SeatNumber('C', 2)
        val seats = screening.reserve(listOf(seatNumber))
        val reservations = Reservations().addReservation(Reservation(screening, seats))
        repository.save(reservations)

        val reloadedScreening = loadFirstScreening()

        assertThat(reloadedScreening.availableSeats().seatNumbers()).doesNotContain(seatNumber)
    }

    @Test
    fun `이미 예약된 좌석을 다시 예약하면 예외가 발생한다`() {
        val screening = loadFirstScreening()
        val seatNumber = SeatNumber('C', 2)
        val seats = screening.reserve(listOf(seatNumber))
        val reservations = Reservations().addReservation(Reservation(screening, seats))
        repository.save(reservations)

        val reloadedScreening = loadFirstScreening()

        assertThatThrownBy { reloadedScreening.reserve(listOf(seatNumber)) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    private fun loadFirstScreening() =
        screeningRepository
            .findByMovieIdAndDate(
                movieRepository.findIdByTitle("F1 더 무비")!!,
                LocalDate.of(2025, 9, 20),
            ).first()
}
