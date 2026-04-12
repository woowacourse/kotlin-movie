package movie.domain.seat

class SeatInputParser {
    fun parse(raw: String): SeatPositions =
        SeatPositions(
            raw
                .split(",")
                .map { it.trim() }
                .map { parseSeatPosition(it) },
        )

    private fun parseSeatPosition(token: String): SeatPosition {
        val match = SEAT_FORMAT.matchEntire(token)
            ?: throw IllegalArgumentException("유효하지 않은 좌석 형식입니다: $token")

        val row = SeatRow(match.groupValues[1].uppercase())
        val column = SeatColumn(match.groupValues[2].toInt())

        return SeatPosition(row, column)
    }

    companion object {
        private val SEAT_FORMAT = Regex("^([A-Za-z])(\\d+)$")
    }
}
