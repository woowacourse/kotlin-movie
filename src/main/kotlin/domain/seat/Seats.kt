package domain.seat

class Seats(
    private val seats: List<Seat>,
) {
    init {
        require(seats.isNotEmpty()) { "좌석 목록은 비어 있을 수 없습니다." }
    }

    fun hasSeat(seat: Seat): Boolean = seats.contains(seat)

    fun size(): Int = seats.size

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
            for ((row, grade) in layout) {
                for (column in 1..4) {
                    seats.add(Seat(row, column, grade))
                }
            }
            return Seats(seats)
        }
    }
}
