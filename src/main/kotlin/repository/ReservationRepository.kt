package repository

import domain.reservation.TicketBucket
import java.sql.Connection
import java.sql.Statement

class ReservationRepository(
    private val connection: Connection,
) {
    fun save(ticketBucket: TicketBucket) {
        ticketBucket.tickets.forEach { ticket ->
            connection
                .prepareStatement(
                    "INSERT INTO RESERVATION (screening_id) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS,
                ).use { reservationPs ->
                    reservationPs.setInt(1, ticket.screening.id.toInt())
                    reservationPs.executeUpdate()

                    val reservationId =
                        reservationPs.generatedKeys.let {
                            check(it.next()) { "예약 저장에 실패했습니다." }
                            it.getInt(1)
                        }

                    connection
                        .prepareStatement(
                            """INSERT INTO RESERVATION_SEAT (reservation_id, "row", col) VALUES (?, ?, ?)""",
                        ).use { seatPs ->
                            ticket.seatPositions.positions.forEach { position ->
                                seatPs.setInt(1, reservationId)
                                seatPs.setString(2, position.row.name)
                                seatPs.setInt(3, position.column.value)
                                seatPs.executeUpdate()
                            }
                        }
                }
        }
    }
}
