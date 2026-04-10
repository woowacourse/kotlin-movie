package movie.domain.seat

class Seats(
    private val seats: List<Seat>,
) {
    init {
        require(seats.isNotEmpty()) { "좌석 목록은 비어 있을 수 없습니다." }
    }

    fun hasSeat(seat: Seat): Boolean = seats.contains(seat)

    fun size(): Int = seats.size

    fun findSeat(
        row: SeatRow,
        column: SeatColumn,
    ): Seat =
        seats.find { it.seatRow == row && it.seatColumn == column }
            ?: throw IllegalArgumentException("존재하지 않는 좌석입니다.")

    companion object {
        fun createDefault(): Seats {
            val seats = mutableListOf<Seat>()
            val layout =
                mapOf(
                    "A" to SeatGrade.B,
                    "B" to SeatGrade.B,
                    "C" to SeatGrade.S,
                    "D" to SeatGrade.S,
                    "E" to SeatGrade.A,
                )
            for ((seatRow, grade) in layout) {
                for (seatColumn in 1..4) {
                    seats.add(Seat(SeatRow(seatRow), SeatColumn(seatColumn), grade))
                }
            }
            return Seats(seats)
        }
    }
}
