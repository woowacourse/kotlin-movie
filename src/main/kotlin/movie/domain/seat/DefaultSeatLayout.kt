package movie.domain.seat

import movie.domain.seat.number.Column
import movie.domain.seat.number.Row
import movie.domain.seat.number.SeatNumber
import movie.domain.seat.rank.ARank
import movie.domain.seat.rank.SRank

class DefaultSeatLayout : SeatLayout {
    override fun createSeats(): List<Seat> {
        val rows = 'A'..'F'
        val cols = 1..5

        return rows.flatMap { row ->
            cols.map { col ->
                Seat(
                    seatNumber =
                        SeatNumber(
                            row = Row(row),
                            col = Column(col),
                        ),
                    rank = if (row in 'A'..'B') ARank() else SRank(),
                )
            }
        }
    }
}
