package movie.domain.seat

import movie.domain.amount.Price

class Seats(
    val seats: Set<Seat>,
) {
    val size: Int
        get() = seats.size

    val totalPrice: Price
        get() =
            seats
                .map { it.grade.price }
                .reduce { acc, price -> acc + price }

    fun hasSeat(seat: Seat): Boolean = seats.contains(seat)

    fun size(): Int = seats.size

    fun findSeat(
        row: String,
        column: Int,
    ): Seat =
        seats.find { it.row == row && it.column == column }
            ?: throw IllegalArgumentException("존재하지 않는 좌석입니다.")

    fun isAvailable(seat: Seat): Boolean = seats.none { it.row == seat.row && it.column == seat.column }

    fun distinct(): Set<Seat> = seats.toSet()

    companion object {
        fun createDefault(): Seats {
            val seats = mutableSetOf<Seat>()
            val layout =
                mapOf(
                    "A" to SeatGrade.B,
                    "B" to SeatGrade.B,
                    "C" to SeatGrade.S,
                    "D" to SeatGrade.S,
                    "E" to SeatGrade.A,
                )
            for ((row, grade) in layout) {
                for (column in 1..4) {
                    seats.add(Seat(row, column, grade))
                }
            }
            return Seats(seats)
        }
    }
}
