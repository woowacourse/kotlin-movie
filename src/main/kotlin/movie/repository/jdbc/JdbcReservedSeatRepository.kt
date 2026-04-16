package movie.repository.jdbc

import movie.database.DatabaseConnectionFactory
import movie.model.screening.Screening
import movie.model.seat.Seat
import movie.model.seat.SeatNumber
import movie.model.seat.Seats
import movie.repository.ReservedSeatRepository

class JdbcReservedSeatRepository(
    private val isLocal: Boolean,
) : ReservedSeatRepository {
    override fun save(
        screening: Screening,
        seats: Seats,
    ) {
        val connection = DatabaseConnectionFactory.createConnection(isLocal)

        connection.use { conn ->
            val sql =
                """
                INSERT INTO reserved_seat (screening_id, reserved_seats)
                VALUES (?, ?)
                """.trimIndent()

            conn.prepareStatement(sql).use { pStatement ->
                val screeningId =
                    requireNotNull(screening.id) {
                        "회차에 아이디가 없습니다."
                    }
                val reservedSeats = seats.seatNumbers.joinToString(",") { "${it.row}${it.column}" }

                pStatement.setLong(1, screeningId)
                pStatement.setString(2, reservedSeats)

                pStatement.executeUpdate()
            }
        }
    }

    override fun findSeatsByScreening(screening: Screening): Seats {
        val connection = DatabaseConnectionFactory.createConnection(isLocal)
        val seats = mutableListOf<Seat>()

        connection.use { conn ->
            val sql =
                """
                SELECT *
                FROM reserved_seat r
                WHERE r.screening_id = ?
                """.trimIndent()

            conn.prepareStatement(sql).use { pStatement ->
                val screeningId =
                    requireNotNull(screening.id) {
                        "회차에 아이디가 없습니다."
                    }

                pStatement.setLong(1, screeningId)
                val resultSet = pStatement.executeQuery()

                while (resultSet.next()) {
                    val reservedSeats =
                        resultSet
                            .getString("reserved_seats")
                            .split(",")
                            .map { it.trim() }
                            .filter { it.isNotEmpty() }
                            .map { SeatNumber(it[0], it.substring(1).toInt()) }
                            .map {
                                screening.seatMap.screen.seats
                                    .findSeat(it)
                            }

                    seats.addAll(reservedSeats)
                }
            }
        }
        return Seats(seats)
    }
}
