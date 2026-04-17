package movie.data.db.reservation

import movie.domain.amount.PaymentResult
import movie.domain.payment.PaymentMethod
import movie.domain.reservation.Reservations
import movie.domain.seat.Seat
import java.sql.Connection
import java.sql.Statement

class ReservationOrderRepository(
    private val connection: Connection,
) {
    fun save(
        reservations: Reservations,
        paymentResult: PaymentResult,
        paymentMethod: PaymentMethod,
    ): Long {
        val orderId = insertReservationOrder(paymentResult, paymentMethod)

        reservations.getReservations().forEach { reservation ->
            val reservationId = insertReservation(orderId, reservation.screening.id)

            reservation.selectedSeats.getSeats().forEach { seat ->
                insertReservationSeat(reservationId, seat)
            }
        }

        return orderId
    }

    private fun insertReservationOrder(
        paymentResult: PaymentResult,
        paymentMethod: PaymentMethod,
    ): Long {
        val sql = "insert into reservation_orders(used_points, payment_method, total_price) values (?, ?, ?)"

        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { statement ->
            statement.setInt(1, paymentResult.usedPoint.value)
            statement.setString(2, paymentMethod.toDbValue())
            statement.setInt(3, paymentResult.totalPrice.value)
            statement.executeUpdate()

            statement.generatedKeys.use { keys ->
                require(keys.next()) { "예매 주문 저장에 실패했습니다." }
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
                require(keys.next()) { "예매 정보 저장에 실패했습니다." }
                return keys.getLong(1)
            }
        }
    }

    private fun insertReservationSeat(
        reservationId: Long,
        seat: Seat,
    ) {
        val sql = "insert into reservation_seats(reservation_id, seat_row, seat_column, seat_grade) values (?, ?, ?, ?)"

        connection.prepareStatement(sql).use { statement ->
            statement.setLong(1, reservationId)
            statement.setString(2, seat.row)
            statement.setInt(3, seat.column)
            statement.setString(4, seat.grade.name)
            statement.executeUpdate()
        }
    }

    private fun PaymentMethod.toDbValue(): String =
        when (this) {
            is PaymentMethod.CreditCard -> "CREDIT_CARD"
            is PaymentMethod.Cash -> "CASH"
        }
}
