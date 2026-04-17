package movie.data.db.reservation

import movie.data.db.SchemaInitializer
import movie.domain.amount.PaymentResult
import movie.domain.amount.Point
import movie.domain.amount.Price
import movie.domain.movie.Movie
import movie.domain.payment.PaymentMethod
import movie.domain.reservation.Reservation
import movie.domain.reservation.Reservations
import movie.domain.screening.Screen
import movie.domain.screening.Screening
import movie.domain.screening.ScreeningDateTime
import movie.domain.screening.Screenings
import movie.domain.seat.ReservedSeats
import movie.domain.seat.Seat
import movie.domain.seat.SeatGrade
import movie.domain.seat.Seats
import movie.domain.seat.SelectedSeats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDateTime

class ReservationOrderRepositoryTest {
    private lateinit var connection: Connection
    private lateinit var repository: ReservationOrderRepository

    @BeforeEach
    fun setUp() {
        connection =
            DriverManager.getConnection(
                "jdbc:h2:mem:test-${System.nanoTime()}",
                "sa",
                "",
            )

        SchemaInitializer.initialize(connection)
        repository = ReservationOrderRepository(connection)

        insertMovie(1L, "F1 더무비", 120)
        insertScreening(1L, 1L, 1, LocalDateTime.of(2026, 1, 1, 10, 0), LocalDateTime.of(2026, 1, 1, 12, 0))
        insertScreening(2L, 1L, 1, LocalDateTime.of(2026, 1, 1, 14, 0), LocalDateTime.of(2026, 1, 1, 16, 0))
    }

    @Test
    fun `예매 주문 정보를 저장할 수 있다`() {
        val reservations = createReservations(screeningId = 1L)
        val paymentResult = PaymentResult(totalPrice = Price(10000), usedPoint = Point(1000))

        repository.save(reservations, paymentResult, PaymentMethod.CreditCard)

        connection.createStatement().use { statement ->
            statement.executeQuery("select used_points, payment_method, total_price from reservation_orders").use { resultSet ->
                assertThat(resultSet.next()).isTrue()
                assertThat(resultSet.getInt("used_points")).isEqualTo(1000)
                assertThat(resultSet.getString("payment_method")).isEqualTo("CREDIT_CARD")
                assertThat(resultSet.getInt("total_price")).isEqualTo(10000)
            }
        }
    }

    @Test
    fun `예매 주문 시 상영별 예매 정보가 저장된다`() {
        val reservation1 = createReservation(screeningId = 1L)
        val reservation2 = createReservation(screeningId = 2L)
        val reservations = Reservations(listOf(reservation1, reservation2))
        val paymentResult = PaymentResult(totalPrice = Price(30000), usedPoint = Point(0))

        repository.save(reservations, paymentResult, PaymentMethod.Cash)

        connection.createStatement().use { statement ->
            statement.executeQuery("select reservation_order_id, screening_id from reservations order by screening_id").use { resultSet ->
                assertThat(resultSet.next()).isTrue()
                assertThat(resultSet.getLong("screening_id")).isEqualTo(1L)
                assertThat(resultSet.next()).isTrue()
                assertThat(resultSet.getLong("screening_id")).isEqualTo(2L)
            }
        }
    }

    @Test
    fun `예매 주문 시 좌석 정보가 저장된다`() {
        val seats =
            Seats(
                setOf(
                    Seat("A", 1, SeatGrade.B),
                    Seat("A", 2, SeatGrade.B),
                ),
            )
        val reservations = createReservations(screeningId = 1L, seats = seats)
        val paymentResult = PaymentResult(totalPrice = Price(30000), usedPoint = Point(0))

        repository.save(reservations, paymentResult, PaymentMethod.CreditCard)

        connection.createStatement().use { statement ->
            statement.executeQuery("select seat_row, seat_column, seat_grade from reservation_seats order by seat_row").use { resultSet ->
                assertThat(resultSet.next()).isTrue()
                assertThat(resultSet.getString("seat_row")).isEqualTo("A")
                assertThat(resultSet.getInt("seat_column")).isEqualTo(1)
                assertThat(resultSet.getString("seat_grade")).isEqualTo("B")

                assertThat(resultSet.next()).isTrue()
                assertThat(resultSet.getString("seat_row")).isEqualTo("A")
                assertThat(resultSet.getInt("seat_column")).isEqualTo(2)
                assertThat(resultSet.getString("seat_grade")).isEqualTo("B")
            }
        }
    }

    @Test
    fun `현금 결제 수단이 올바르게 저장된다`() {
        val reservations = createReservations(screeningId = 1L)
        val paymentResult = PaymentResult(totalPrice = Price(12000), usedPoint = Point(0))

        repository.save(reservations, paymentResult, PaymentMethod.Cash)

        connection.createStatement().use { statement ->
            statement.executeQuery("select payment_method from reservation_orders").use { resultSet ->
                assertThat(resultSet.next()).isTrue()
                assertThat(resultSet.getString("payment_method")).isEqualTo("CASH")
            }
        }
    }

    private fun createReservations(
        screeningId: Long,
        seats: Seats = Seats(setOf(Seat("A", 1, SeatGrade.B))),
    ): Reservations = Reservations(listOf(createReservation(screeningId, seats)))

    private fun createReservation(
        screeningId: Long,
        seats: Seats = Seats(setOf(Seat("A", 1, SeatGrade.B))),
    ): Reservation {
        val movie =
            Movie(
                id = 1L,
                title = "F1 더 무비",
                screenings = Screenings(emptyList()),
            )
        val screening =
            Screening(
                id = screeningId,
                screen = Screen(1),
                screeningDateTime =
                    ScreeningDateTime(
                        LocalDateTime.of(2026, 1, 1, 10, 0).plusHours((screeningId - 1) * 4),
                        LocalDateTime.of(2026, 1, 1, 12, 0).plusHours((screeningId - 1) * 4),
                    ),
                reservedSeats = ReservedSeats(Seats(emptySet())),
            )
        return Reservation(movie, screening, SelectedSeats(seats))
    }

    private fun insertMovie(
        id: Long,
        title: String,
        runningTimeMiutes: Int,
    ) {
        val sql = "insert into movies(id, title, running_time_minutes) values (?, ?, ?)"
        connection.prepareStatement(sql).use { statement ->
            statement.setLong(1, id)
            statement.setString(2, title)
            statement.setInt(3, runningTimeMiutes)
            statement.executeUpdate()
        }
    }

    private fun insertScreening(
        id: Long,
        movieId: Long,
        screenId: Int,
        startAt: LocalDateTime,
        endAt: LocalDateTime,
    ) {
        val sql = "insert into screenings(id, movie_id, screen_id, start_at, end_at) values (?, ?, ?, ?, ?)"

        connection.prepareStatement(sql).use { statement ->
            statement.setLong(1, id)
            statement.setLong(2, movieId)
            statement.setInt(3, screenId)
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(startAt))
            statement.setTimestamp(5, java.sql.Timestamp.valueOf(endAt))
            statement.executeUpdate()
        }
    }
}
