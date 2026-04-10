@file:Suppress("NonAsciiCharacters")

package model.seat

import model.Money
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class SeatsTest {
    @Test
    fun `존재하지 않는 좌석을 찾으면 예외가 발생한다`() {
        val seat = Seat(SeatNumber('B', 1), SeatGrade.S)
        val seats = Seats(listOf(seat))

        assertThrows(IllegalArgumentException::class.java) {
            seats.findSeat(SeatNumber('A', 1))
        }
    }

    @Test
    fun `A등급과 B등급 좌석의 총 가격이 올바르게 합산된다`() {
        val seatA = Seat(SeatNumber('B', 1), SeatGrade.A)
        val seatB = Seat(SeatNumber('B', 2), SeatGrade.B)
        val seats = Seats(listOf(seatA, seatB))

        assertThat(seats.calculateTotalPrice()).isEqualTo(Money(27_000))
    }

    @Test
    fun `예약된 좌석 번호를 제외한 나머지 좌석을 반환한다`() {
        val seat1 = Seat(SeatNumber('A', 1), SeatGrade.B)
        val seat2 = Seat(SeatNumber('A', 2), SeatGrade.B)
        val seats = Seats(listOf(seat1, seat2))

        val available = seats.excludeReserved(setOf(SeatNumber('A', 1)))

        assertThat(available.calculateTotalPrice()).isEqualTo(Money(12_000))
    }
}
