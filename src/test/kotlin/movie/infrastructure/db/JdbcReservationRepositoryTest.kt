package movie.infrastructure.db

import movie.domain.amount.Money
import movie.domain.amount.Point
import movie.domain.movie.MovieTitle
import movie.domain.reservation.Reservation
import movie.domain.reservation.Reservations
import movie.domain.screening.Screen
import movie.domain.screening.ScreenId
import movie.domain.screening.Screening
import movie.domain.screening.ScreeningDateTime
import movie.domain.screening.ScreeningSlot
import movie.domain.seat.ReservatedSeats
import movie.domain.seat.Seat
import movie.domain.seat.SeatColumn
import movie.domain.seat.SeatGrade
import movie.domain.seat.SeatRow
import movie.domain.seat.Seats
import movie.domain.seat.SelectedSeats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.time.LocalDate
import java.time.LocalTime

class JdbcReservationRepositoryTest {
    private lateinit var connection: Connection
    private lateinit var reservationRepository: JdbcReservationRepository
    private lateinit var reservedSeatRepository: JdbcReservedSeatRepository

    @BeforeEach
    fun setUp() {
        connection = DatabaseConnector.connectTest()
        DatabaseInitializer(connection).initialize()
        reservedSeatRepository = JdbcReservedSeatRepository(connection)
        reservationRepository = JdbcReservationRepository(connection, reservedSeatRepository)
    }

    @AfterEach
    fun tearDown() {
        connection.createStatement().use { it.execute("DROP ALL OBJECTS") }
        connection.close()
    }

    private fun createReservation(screeningId: Long): Reservation {
        val screen = Screen(ScreenId(1), Seats.createDefault())
        val screening =
            Screening(
                id = screeningId,
                movie = MovieTitle("F1 더 무비"),
                slot =
                    ScreeningSlot(
                        screen,
                        ScreeningDateTime(
                            LocalDate.of(2025, 9, 20),
                            LocalTime.of(10, 20),
                            LocalTime.of(12, 20),
                        ),
                    ),
                reservatedSeats = ReservatedSeats(emptyList()),
            )
        val selectedSeats =
            SelectedSeats(
                listOf(
                    Seat(SeatRow("A"), SeatColumn(1), SeatGrade.B),
                    Seat(SeatRow("A"), SeatColumn(2), SeatGrade.B),
                ),
            )
        return Reservation(screening, selectedSeats)
    }

    @Test
    fun `예매를 저장할 수 있다`() {
        val reservation = createReservation(101L)
        val reservations = Reservations(listOf(reservation))

        val reservationId =
            reservationRepository.save(
                reservations,
                Money(24000),
                Point(1000),
                "CREDIT_CARD",
            )

        assertThat(reservationId).isGreaterThan(0L)
    }

    @Test
    fun `저장 후 reservation 테이블에 데이터가 존재한다`() {
        val reservation = createReservation(101L)
        val reservations = Reservations(listOf(reservation))

        reservationRepository.save(
            reservations,
            Money(24000),
            Point(1000),
            "CREDIT_CARD",
        )

        val count =
            connection.createStatement().use { stmt ->
                stmt.executeQuery("SELECT COUNT(*) FROM reservation").use { rs ->
                    rs.next()
                    rs.getInt(1)
                }
            }
        assertThat(count).isEqualTo(1)
    }

    @Test
    fun `저장 후 reservation_item 테이블에 데이터가 존재한다`() {
        val reservation = createReservation(101L)
        val reservations = Reservations(listOf(reservation))

        reservationRepository.save(
            reservations,
            Money(24000),
            Point(1000),
            "CREDIT_CARD",
        )

        val count =
            connection.createStatement().use { stmt ->
                stmt.executeQuery("SELECT COUNT(*) FROM reservation_item").use { rs ->
                    rs.next()
                    rs.getInt(1)
                }
            }
        assertThat(count).isEqualTo(1)
    }

    @Test
    fun `저장 후 reservation_seat 테이블에 좌석이 저장된다`() {
        val reservation = createReservation(101L)
        val reservations = Reservations(listOf(reservation))

        reservationRepository.save(
            reservations,
            Money(24000),
            Point(1000),
            "CREDIT_CARD",
        )

        val count =
            connection.createStatement().use { stmt ->
                stmt.executeQuery("SELECT COUNT(*) FROM reservation_seat").use { rs ->
                    rs.next()
                    rs.getInt(1)
                }
            }
        assertThat(count).isEqualTo(2)
    }

    @Test
    fun `저장 후 reserved_seat 테이블에도 좌석이 등록된다`() {
        val reservation = createReservation(101L)
        val reservations = Reservations(listOf(reservation))

        reservationRepository.save(
            reservations,
            Money(24000),
            Point(1000),
            "CREDIT_CARD",
        )

        val reservedSeats = reservedSeatRepository.findAllByScreeningId(101L)
        assertThat(reservedSeats).hasSize(2)
    }

    @Test
    fun `여러 예매를 한꺼번에 저장할 수 있다`() {
        val screen = Screen(ScreenId(1), Seats.createDefault())
        val selectedSeats =
            SelectedSeats(
                listOf(
                    Seat(SeatRow("A"), SeatColumn(1), SeatGrade.B),
                    Seat(SeatRow("A"), SeatColumn(2), SeatGrade.B),
                ),
            )

        val screening1 =
            Screening(
                id = 101L,
                movie = MovieTitle("F1 더 무비"),
                slot =
                    ScreeningSlot(
                        screen,
                        ScreeningDateTime(
                            LocalDate.of(2025, 9, 20),
                            LocalTime.of(10, 20),
                            LocalTime.of(12, 20),
                        ),
                    ),
                reservatedSeats = ReservatedSeats(emptyList()),
            )

        val screening2 =
            Screening(
                id = 201L,
                movie = MovieTitle("토이 스토리"),
                slot =
                    ScreeningSlot(
                        screen,
                        ScreeningDateTime(
                            LocalDate.of(2025, 9, 20),
                            LocalTime.of(13, 30),
                            LocalTime.of(15, 30),
                        ),
                    ),
                reservatedSeats = ReservatedSeats(emptyList()),
            )

        val reservation1 = Reservation(screening1, selectedSeats)
        val reservation2 = Reservation(screening2, selectedSeats)
        val reservations = Reservations(listOf(reservation1, reservation2))

        reservationRepository.save(
            reservations,
            Money(48000),
            Point(0),
            "CASH",
        )

        val itemCount =
            connection.createStatement().use { stmt ->
                stmt.executeQuery("SELECT COUNT(*) FROM reservation_item").use { rs ->
                    rs.next()
                    rs.getInt(1)
                }
            }
        assertThat(itemCount).isEqualTo(2)

        val seatCount =
            connection.createStatement().use { stmt ->
                stmt.executeQuery("SELECT COUNT(*) FROM reservation_seat").use { rs ->
                    rs.next()
                    rs.getInt(1)
                }
            }
        assertThat(seatCount).isEqualTo(4)
    }
}
