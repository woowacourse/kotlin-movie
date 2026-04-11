package domain.timetable.items

import domain.seat.Seat
import domain.seat.items.ColumnNumber
import domain.seat.items.GradeB
import domain.seat.items.RowNumber
import domain.seat.items.SeatGrade
import domain.seat.items.SeatPosition
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SeatsTest {
    @Test
    fun `입력된 좌석 번호가 상영관에 존재하는 좌석이면 true를 반환한다`() {
        val seatA = createSeat()
        val seatB = createSeat(columnNumber = ColumnNumber(2))

        val seats = listOf(seatA, seatB)
        val screenSeats = Seats(seats)

        val result = screenSeats.isExistSeatNumber("A1")

        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 좌석 번호가 상영관에 존재하지 않는 좌석이면 false를 반환한다`() {
        val seatA = createSeat()
        val seatB = createSeat(columnNumber = ColumnNumber(2))

        val seats = listOf(seatA, seatB)
        val screenSeats = Seats(seats)

        val result = screenSeats.isExistSeatNumber("Z11111")

        assertThat(result).isFalse()
    }

    @Test
    fun `입력된 좌석 번호의 상영관에 존재하는 좌석이면 좌석을 반환한다`() {
        val seatA = createSeat()
        val seatB = createSeat(columnNumber = ColumnNumber(2))

        val seats = listOf(seatA, seatB)
        val screenSeats = Seats(seats)

        val result = screenSeats.findSeat("A1")

        assertThat(result).isEqualTo(seatA)
    }

    @Test
    fun `입력된 좌석 번호의 상영관에 존재하지 않는 좌석이면 예외를 발생시킨다`() {
        val seatA = createSeat()
        val seatB = createSeat(columnNumber = ColumnNumber(2))

        val seats = listOf(seatA, seatB)
        val screenSeats = Seats(seats)

        assertThrows<IllegalArgumentException> { screenSeats.findSeat("ZZ11234") }
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
