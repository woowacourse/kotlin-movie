package database.repository

import database.Database
import model.payment.PayType
import model.seat.Seat

class ReservationRepository {
    fun createReservation(): Int {
        val sql = "INSERT INTO reservation () VALUES ()"

        Database.connection().use { connection ->
            connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS).use { preparedStatement ->
                preparedStatement.executeUpdate()
                preparedStatement.generatedKeys.use { keys ->
                    keys.next()
                    return keys.getInt(1)
                }
            }
        }
    }

    fun saveSeat(
        reservationId: Int,
        screeningId: Int,
        seat: Seat,
    ) {
        val sql =
            """
            INSERT INTO reservation_seat (reservation_id, screening_id, seat)
            VALUES (?, ?, ?)
            """.trimIndent()

        Database.connection().use { connection ->
            connection.prepareStatement(sql).use { preparedStatement ->
                preparedStatement.setInt(1, reservationId)
                preparedStatement.setInt(2, screeningId)
                preparedStatement.setString(3, "${seat.row}${seat.column}")
                preparedStatement.executeUpdate()
            }
        }
    }

    fun updatePayment(
        reservationId: Int,
        totalPrice: Int,
        usedPoint: Int,
        payType: PayType,
    ) {
        val sql =
            """
            UPDATE reservation
            SET total_price = ?, used_point = ?, payment_method = ?
            WHERE id = ?
            """.trimIndent()

        Database.connection().use { connection ->
            connection.prepareStatement(sql).use { preparedStatement ->
                preparedStatement.setInt(1, totalPrice)
                preparedStatement.setInt(2, usedPoint)
                preparedStatement.setString(3, payType.name)
                preparedStatement.setInt(4, reservationId)
                preparedStatement.executeUpdate()
            }
        }
    }

    fun findByScreeningId(screeningId: Int): List<String> {
        val sql =
            """
            SELECT seat                                                                                                                                                                      
            FROM reservation_seat
            WHERE screening_id = ?
            """.trimIndent()

        Database.connection().use { connection ->
            connection.prepareStatement(sql).use { preparedStatement ->
                preparedStatement.setInt(1, screeningId)
                preparedStatement.executeQuery().use { result ->
                    val seats = mutableListOf<String>()
                    while (result.next()) {
                        seats += result.getString("seat")
                    }
                    return seats
                }
            }
        }
    }

    fun delete(reservationId: Int) {
        Database.connection().use { connection ->
            connection.prepareStatement("DELETE FROM reservation_seat WHERE reservation_id = ?").use {
                it.setInt(1, reservationId)
                it.executeUpdate()
            }
            connection.prepareStatement("DELETE FROM reservation WHERE id = ?").use {
                it.setInt(1, reservationId)
                it.executeUpdate()
            }
        }
    }
}
