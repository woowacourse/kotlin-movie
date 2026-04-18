package movie.domain.seat

import movie.error.SeatErrorMessage

data class SeatNumber(
    val row: Char,
    val col: Int,
) {
    init {
        require(row in 'A'..'Z') { SeatErrorMessage.INVALID_ROW }
        require(col > 0) { SeatErrorMessage.INVALID_COL }
    }

    constructor(input: String) : this(
        row = input.first(),
        col =
            input.substring(1).toIntOrNull()
                ?: throw IllegalArgumentException(SeatErrorMessage.COL_NOT_INTEGER),
    )
}
