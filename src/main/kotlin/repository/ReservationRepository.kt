package repository

import domain.reservation.Reservation
import org.springframework.stereotype.Repository
import java.sql.Statement
import javax.sql.DataSource

@Repository
class ReservationRepository(private val dataSource: DataSource) {

    fun save(reservation: Reservation): Long {
        val sqlReservation = "INSERT INTO reservations (used_points, payment_method, total_price) VALUES (?, ?, ?)"
        val sqlLineItem = "INSERT INTO reserved_seats (reservation_id, screening_id, seat_row, seat_column) VALUES (?, ?, ?, ?)"

        dataSource.connection.use { connection ->
            try {
                connection.autoCommit = false

                val reservationId = connection.prepareStatement(sqlReservation, Statement.RETURN_GENERATED_KEYS).use { stmt ->
                    stmt.setInt(1, reservation.usedPoints.amount)
                    stmt.setString(2, reservation.paymentMethod.name)
                    stmt.setInt(3, reservation.totalPrice.amount)
                    stmt.executeUpdate()

                    val rs = stmt.generatedKeys
                    if (rs.next()) {
                        rs.getLong(1)
                    } else {
                        throw IllegalStateException("예매 ID를 생성할 수 없습니다.")
                    }
                }

                connection.prepareStatement(sqlLineItem).use { stmt ->
                    for (ticket in reservation.tickets) {
                        val screeningId = ticket.screening.id ?: throw IllegalArgumentException("상영 ID가 없습니다.")
                        for (position in ticket.seatPositions.positions) {
                            stmt.setLong(1, reservationId)
                            stmt.setLong(2, screeningId)
                            stmt.setString(3, position.row.value)
                            stmt.setInt(4, position.column.value)
                            stmt.addBatch()
                        }
                    }
                    stmt.executeBatch()
                }

                connection.commit()
                return reservationId
            } catch (e: Exception) {
                connection.rollback()
                throw e
            } finally {
                connection.autoCommit = true
            }
        }
    }
}
