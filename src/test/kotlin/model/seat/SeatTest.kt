package model.seat

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SeatTest {
    @Test
    fun `좌석은 행(알파벳)과 열(숫자)로 구성된 이름과 예약여부, 좌석등급을 갖는다`() {
        val row = "A"
        val column = 10
        val isReserved = false
        val seatRank = SeatRank.B_RANK
        val seat = Seat(row, column, isReserved, seatRank)

        assertThat(seat.row).isEqualTo("A")
        assertThat(seat.column).isEqualTo(10)
        assertThat(seat.isReserved).isFalse
        assertThat(seat.seatRank).isEqualTo(SeatRank.B_RANK)
    }

    @Test
    fun `이미 예약된 좌석을 선택한다면 에러가 발생한다`() {
        val row = "A"
        val column = 10
        val isReserved = true
        val seatRank = SeatRank.B_RANK
        val reservedSeat = Seat(row, column, isReserved, seatRank)

        assertThrows<IllegalArgumentException> {
            reservedSeat.reserve()
        }
    }

    @Test
    fun `예약되지 않은 좌석을 예약한다면 예약이 완료된 좌석이 반환된다`() {
        val row = "A"
        val column = 10
        val isReserved = false
        val seatRank = SeatRank.B_RANK
        val notReservedSeat = Seat(row, column, isReserved, seatRank)

        val reservedSeat = notReservedSeat.reserve()

        assertThat(reservedSeat.isReserved).isTrue
    }
}
