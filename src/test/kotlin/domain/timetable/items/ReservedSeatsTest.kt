package domain.timetable.items

import domain.money.Money
import domain.seat.Seat
import domain.seat.items.ColumnNumber
import domain.seat.items.GradeB
import domain.seat.items.RowNumber
import domain.seat.items.SeatGrade
import domain.seat.items.SeatPosition
import org.assertj.core.api.Assertions.assertThat
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

    @Test
    fun `예약된 좌석들의 가격의 합계를 반환한다`() {
        val reservedSeats = ReservedSeats()
        val seatA1 = createSeat()
        val seatA2 = createSeat(columnNumber = ColumnNumber(2))
        val seatA3 = createSeat(columnNumber = ColumnNumber(3))
        reservedSeats.addSeat(seatA1)
        reservedSeats.addSeat(seatA2)
        reservedSeats.addSeat(seatA3)
        val result = reservedSeats.sumPrice()

        assertThat(result).isEqualTo(Money(36_000))
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
