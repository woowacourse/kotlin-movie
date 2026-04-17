package movie.data.db.reservation

import movie.data.db.SchemaInitializer
import movie.domain.seat.Seat
import movie.domain.seat.SeatGrade
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.sql.Timestamp
import java.time.LocalDateTime

class ReservedSeatRepositoryTest {
    private lateinit var connection: Connection
    private lateinit var repository: ReservedSeatRepository

    @BeforeEach
    fun setUp() {
        connection =
            DriverManager.getConnection(
                "jdbc:h2:mem:test-${System.nanoTime()}",
                "sa",
                "",
            )

        SchemaInitializer.initialize(connection)
        repository = ReservedSeatRepository(connection)

        insertMovie(1L, "F1 더 무비", 120)
        insertScreening(1L, 1L, 1, LocalDateTime.of(2026, 1, 1, 10, 0), LocalDateTime.of(2026, 1, 1, 12, 0))
        insertScreening(2L, 1L, 1, LocalDateTime.of(2026, 1, 1, 14, 0), LocalDateTime.of(2026, 1, 1, 16, 0))
    }

    @Test
    fun `특정 상영의 예약된 좌석을 조회할 수 있다`() {
        val orderId = insertReservationOrder(0, "CREDIT_CARD", 30000)
        val reservationId = insertReservation(orderId, 1L)
        insertReservationSeat(reservationId, "A", 1, "B")
        insertReservationSeat(reservationId, "C", 3, "S")

        val seats = repository.findByScreeningId(1L)

        assertThat(seats).hasSize(2)
        assertThat(seats).contains(Seat("A", 1, SeatGrade.B))
        assertThat(seats).contains(Seat("C", 3, SeatGrade.S))
    }

    private fun insertMovie(
        id: Long,
        title: String,
        runningTimeMinutes: Int,
    ) {
        val sql = "insert into movies(id, title, running_time_minutes) values (?, ?, ?)"
        connection.prepareStatement(sql).use { statement ->
            statement.setLong(1, id)
            statement.setString(2, title)
            statement.setInt(3, runningTimeMinutes)
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
            statement.setTimestamp(4, Timestamp.valueOf(startAt))
            statement.setTimestamp(5, Timestamp.valueOf(endAt))
            statement.executeUpdate()
        }
    }

    private fun insertReservationOrder(
        usedPoints: Int,
        paymentMethod: String,
        totalPrice: Int,
    ): Long {
        val sql = "insert into reservation_orders(used_points, payment_method, total_price) values (?, ?, ?)"
        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { statement ->
            statement.setInt(1, usedPoints)
            statement.setString(2, paymentMethod)
            statement.setInt(3, totalPrice)
            statement.executeUpdate()
            statement.generatedKeys.use { keys ->
                keys.next()
                return keys.getLong(1)
            }
        }
    }

    private fun insertReservation(
        orderId: Long,
        screeningId: Long,
    ): Long {
        val sql = "insert into reservations(reservation_order_id, screening_id) values (?, ?)"
        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { statement ->
            statement.setLong(1, orderId)
            statement.setLong(2, screeningId)
            statement.executeUpdate()
            statement.generatedKeys.use { keys ->
                keys.next()
                return keys.getLong(1)
            }
        }
    }

    private fun insertReservationSeat(
        reservationId: Long,
        row: String,
        column: Int,
        grade: String,
    ) {
        val sql = "insert into reservation_seats(reservation_id, seat_row, seat_column, seat_grade) values (?, ?, ?, ?)"
        connection.prepareStatement(sql).use { statement ->
            statement.setLong(1, reservationId)
            statement.setString(2, row)
            statement.setInt(3, column)
            statement.setString(4, grade)
            statement.executeUpdate()
        }
    }
}
