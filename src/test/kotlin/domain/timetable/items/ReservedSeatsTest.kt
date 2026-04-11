package domain.timetable.items

import domain.seat.Seat
import domain.seat.items.ColumnNumber
import domain.seat.items.GradeB
import domain.seat.items.RowNumber
import domain.seat.items.SeatGrade
import domain.seat.items.SeatPosition
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class ReservedSeatsTest {
    @Test
    fun `입력된 좌석 번호를 reserved seats에 추가한다`() {
        val reservedSeats = ReservedSeats()
        val seatA1 = createSeat()

        assertDoesNotThrow { reservedSeats.addSeat(seatA1) }
    }

    @Test
    fun `입력된 좌석이 reserved seats에 이미 있다면 예외를 발생한다`() {
        val reservedSeats = ReservedSeats()
        val seatA1 = createSeat()
        reservedSeats.addSeat(seatA1)

        assertThrows<IllegalArgumentException> { reservedSeats.addSeat(seatA1) }
    }

    private fun createSeat(
        rowNumber: RowNumber = RowNumber("A"),
        columnNumber: ColumnNumber = ColumnNumber(1),
        grade: SeatGrade = GradeB(),
    ) = Seat(
        seatPosition =
            SeatPosition(
                rowNumber = rowNumber,
                columnNumber = columnNumber,
            ),
        seatGrade = grade,
    )
}
