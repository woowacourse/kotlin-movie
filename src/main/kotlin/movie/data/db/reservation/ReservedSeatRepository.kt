package movie.data.db.reservation

import movie.domain.seat.Seat
import movie.domain.seat.SeatGrade
import java.sql.Connection

class ReservedSeatRepository(
    private val connection: Connection,
) {
    fun findByScreeningId(screeningId: Long): Set<Seat> {
        val sql =
            """
            select rs.seat_row, rs.seat_column, rs.seat_grade
            from reservation_seats rs
            inner join reservations r on rs.reservation_id = r.id
            where r.screening_id = ?
            """.trimIndent()

        val seats = mutableSetOf<Seat>()

        connection.prepareStatement(sql).use { statement ->
            statement.setLong(1, screeningId)

            statement.executeQuery().use { resultSet ->
                while (resultSet.next()) {
                    seats.add(
                        Seat(
                            row = resultSet.getString("seat_row"),
                            column = resultSet.getInt("seat_column"),
                            grade = SeatGrade.valueOf(resultSet.getString("seat_grade")),
                        ),
                    )
                }
            }
        }

        return seats
    }
}
