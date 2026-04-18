package movie.infrastructure.db

import movie.domain.amount.Money
import movie.domain.amount.Point
import movie.domain.reservation.Reservations
import movie.repository.ReservationRepository
import movie.repository.ReservedSeatRepository
import java.sql.Connection
import java.sql.Statement

class JdbcReservationRepository(
    private val connection: Connection,
    private val reservedSeatRepository: ReservedSeatRepository,
) : ReservationRepository {
    override fun save(
        reservations: Reservations,
        totalPrice: Money,
        usedPoints: Point,
        paymentMethod: String,
    ): Long {
        val autoCommit = connection.autoCommit
        connection.autoCommit = false

        try {
            val reservationId = insertReservation(totalPrice, usedPoints, paymentMethod)

            reservations.forEach { reservation ->
                val itemId = insertReservationItem(reservationId, reservation.screeningId())
                reservation.forEachSeat { row, column ->
                    insertReservationSeat(itemId, row, column)
                }
                reservedSeatRepository.saveAll(reservation.screeningId(), reservation.seatList())
            }

            connection.commit()
            return reservationId
        } catch (e: Exception) {
            connection.rollback()
            throw e
        } finally {
            connection.autoCommit = autoCommit
        }
    }

    private fun insertReservation(
        totalPrice: Money,
        usedPoints: Point,
        paymentMethod: String,
    ): Long {
        val sql = "INSERT INTO reservation (used_points, payment_method, total_price) VALUES (?, ?, ?)"

        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { stmt ->
            stmt.setInt(1, usedPoints.value)
            stmt.setString(2, paymentMethod)
            stmt.setInt(3, totalPrice.value)
            stmt.executeUpdate()

            stmt.generatedKeys.use { rs ->
                rs.next()
                return rs.getLong(1)
            }
        }
    }

    private fun insertReservationItem(
        reservationId: Long,
        screeningId: Long,
    ): Long {
        val sql = "INSERT INTO reservation_item (reservation_id, screening_id) VALUES (?, ?)"

        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { stmt ->
            stmt.setLong(1, reservationId)
            stmt.setLong(2, screeningId)
            stmt.executeUpdate()

            stmt.generatedKeys.use { rs ->
                rs.next()
                return rs.getLong(1)
            }
        }
    }

    private fun insertReservationSeat(
        reservationItemId: Long,
        seatRow: String,
        seatColumn: Int,
    ) {
        val sql = "INSERT INTO reservation_seat (reservation_item_id, seat_row, seat_column) VALUES (?, ?, ?)"

        connection.prepareStatement(sql).use { stmt ->
            stmt.setLong(1, reservationItemId)
            stmt.setString(2, seatRow)
            stmt.setInt(3, seatColumn)
            stmt.executeUpdate()
        }
    }
}
