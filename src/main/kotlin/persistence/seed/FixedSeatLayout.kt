package persistence.seed

import domain.seat.Seat
import domain.seat.SeatCoordinate
import domain.seat.SeatGrade
import domain.seat.SeatState

internal object FixedSeatLayout {
    private val templates =
        listOf(
            SeatTemplate('A', 1, SeatGrade.B),
            SeatTemplate('A', 2, SeatGrade.B),
            SeatTemplate('A', 3, SeatGrade.B),
            SeatTemplate('A', 4, SeatGrade.B),
            SeatTemplate('B', 1, SeatGrade.B),
            SeatTemplate('B', 2, SeatGrade.B),
            SeatTemplate('B', 3, SeatGrade.B),
            SeatTemplate('B', 4, SeatGrade.B),
            SeatTemplate('C', 1, SeatGrade.S),
            SeatTemplate('C', 2, SeatGrade.S),
            SeatTemplate('C', 3, SeatGrade.S),
            SeatTemplate('C', 4, SeatGrade.S),
            SeatTemplate('D', 1, SeatGrade.S),
            SeatTemplate('D', 2, SeatGrade.S),
            SeatTemplate('D', 3, SeatGrade.S),
            SeatTemplate('D', 4, SeatGrade.S),
            SeatTemplate('E', 1, SeatGrade.A),
            SeatTemplate('E', 2, SeatGrade.A),
            SeatTemplate('E', 3, SeatGrade.A),
            SeatTemplate('E', 4, SeatGrade.A),
        )

    fun createSeats(reservedSeatKeys: Set<SeatKey> = emptySet()): List<Seat> =
        templates.map { template ->
            Seat(
                coordinate = SeatCoordinate(template.row, template.column),
                grade = template.grade,
                isReserved =
                    if (template.toSeatKey() in reservedSeatKeys) {
                        SeatState.RESERVED
                    } else {
                        SeatState.AVAILABLE
                    },
            )
        }

    fun findSeatGrade(seatKey: SeatKey): SeatGrade = templates.first { it.toSeatKey() == seatKey }.grade

    data class SeatKey(
        val row: Char,
        val column: Int,
    )

    private data class SeatTemplate(
        val row: Char,
        val column: Int,
        val grade: SeatGrade,
    ) {
        fun toSeatKey(): SeatKey = SeatKey(row, column)
    }
}
