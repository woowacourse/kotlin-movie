package movie.infrastructure.db

import movie.domain.seat.Seat
import movie.domain.seat.SeatColumn
import movie.domain.seat.SeatGrade
import movie.domain.seat.SeatRow
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.sql.Connection

class JdbcReservedSeatRepositoryTest {
    private lateinit var connection: Connection
    private lateinit var repository: JdbcReservedSeatRepository

    @BeforeEach
    fun setUp() {
        connection = DatabaseConnector.connectTest()
        DatabaseInitializer(connection).initialize()
        repository = JdbcReservedSeatRepository(connection)
    }

    @AfterEach
    fun tearDown() {
        connection.createStatement().use { it.execute("DROP ALL OBJECTS") }
        connection.close()
    }

    @Test
    fun `상영에 예약된 좌석을 조회할 수 있다`() {
        val seats = repository.findAllByScreeningId(102L)

        assertThat(seats).hasSize(4)
        assertThat(seats).contains(
            Seat(SeatRow("B"), SeatColumn(2), SeatGrade.B),
            Seat(SeatRow("B"), SeatColumn(3), SeatGrade.B),
            Seat(SeatRow("C"), SeatColumn(3), SeatGrade.S),
            Seat(SeatRow("E"), SeatColumn(4), SeatGrade.A),
        )
    }

    @Test
    fun `예약된 좌석이 없으면 빈 리스트를 반환한다`() {
        val seats = repository.findAllByScreeningId(101L)

        assertThat(seats).isEmpty()
    }

    @Test
    fun `좌석을 저장할 수 있다`() {
        val seatsToSave =
            listOf(
                Seat(SeatRow("A"), SeatColumn(1), SeatGrade.B),
                Seat(SeatRow("A"), SeatColumn(2), SeatGrade.B),
            )

        repository.saveAll(101L, seatsToSave)

        val saved = repository.findAllByScreeningId(101L)
        assertThat(saved).hasSize(2)
        assertThat(saved).contains(
            Seat(SeatRow("A"), SeatColumn(1), SeatGrade.B),
            Seat(SeatRow("A"), SeatColumn(2), SeatGrade.B),
        )
    }

    @Test
    fun `이미 예약된 좌석을 다시 저장하면 예외가 발생한다`() {
        val duplicateSeats =
            listOf(
                Seat(SeatRow("B"), SeatColumn(2), SeatGrade.B),
            )

        assertThrows<Exception> {
            repository.saveAll(102L, duplicateSeats)
        }
    }
}
