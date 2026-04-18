package movie.infrastructure.db

import movie.domain.seat.Seat
import movie.domain.seat.SeatColumn
import movie.domain.seat.SeatRow
import movie.domain.seat.Seats
import movie.repository.ReservedSeatRepository
import java.sql.Connection

class JdbcReservedSeatRepository(
    private val connection: Connection,
) : ReservedSeatRepository {
    private val defaultSeats: Seats = Seats.createDefault()

    override fun findAllByScreeningId(screeningId: Long): List<Seat> {
        val sql = "SELECT seat_row, seat_column FROM reserved_seat WHERE screening_id = ?"
        val seats = mutableListOf<Seat>()

        connection.prepareStatement(sql).use { stmt ->
            stmt.setLong(1, screeningId)
            stmt.executeQuery().use { rs ->
                while (rs.next()) {
                    val row = SeatRow(rs.getString("seat_row"))
                    val column = SeatColumn(rs.getInt("seat_column"))
                    val seat = defaultSeats.findSeat(row, column)
                    seats.add(seat)
                }
            }
        }

        return seats
    }

    override fun saveAll(
        screeningId: Long,
        seats: List<Seat>,
    ) {
        val sql = "INSERT INTO reserved_seat (screening_id, seat_row, seat_column) VALUES (?, ?, ?)"

        connection.prepareStatement(sql).use { stmt ->
            seats.forEach { seat ->
                stmt.setLong(1, screeningId)
                stmt.setString(2, seat.seatRow.value)
                stmt.setInt(3, seat.seatColumn.value)
                stmt.addBatch()
            }
            stmt.executeBatch()
        }
    }
}
