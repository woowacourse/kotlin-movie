package movie.repository

import movie.error.SystemErrorMessage
import movie.domain.Reservation
import java.sql.Connection
import java.sql.Statement

class ReservationRepository(private val connection: Connection) {
    fun save(reservation: Reservation): Long {
        val reservationQuery = """
            INSERT INTO reservation (schedule_id, total_price)
            VALUES (?, ?)
        """.trimIndent()

        val seatQuery = """
            INSERT INTO reserved_seat (reservation_id, schedule_id, seat_number)
            VALUES (?, ?, ?)
        """.trimIndent()

        val previousAutoCommit = connection.autoCommit
        connection.autoCommit = false
        try {
            val reservationId = connection.prepareStatement(reservationQuery, Statement.RETURN_GENERATED_KEYS).use { pstmt ->
                pstmt.setLong(1, reservation.schedule.id)
                pstmt.setInt(2, reservation.calculateTotalPrice().amount)
                pstmt.executeUpdate()

                val rs = pstmt.generatedKeys
                if (rs.next()) {
                    rs.getLong(1)
                } else {
                    throw IllegalStateException(SystemErrorMessage.RESERVATION_FAILED)
                }
            }

            connection.prepareStatement(seatQuery).use { pstmt ->
                for (seat in reservation.seats) {
                    pstmt.setLong(1, reservationId)
                    pstmt.setLong(2, reservation.schedule.id)
                    pstmt.setString(3, "${seat.row}${seat.col}")
                    pstmt.addBatch()
                }
                pstmt.executeBatch()
            }

            connection.commit()
            return reservationId
        } catch (e: Exception) {
            connection.rollback()
            throw e
        } finally {
            connection.autoCommit = previousAutoCommit
        }
    }
}
