@file:Suppress("NonAsciiCharacters")

package model.screening

import model.Screen
import model.seat.Seat
import model.seat.SeatGrade
import model.seat.SeatNumber
import model.seat.Seats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class ScreeningSeatMapTest {
    val seat1 = Seat(SeatNumber('A', 1), SeatGrade.B)
    val seat2 = Seat(SeatNumber('A', 2), SeatGrade.B)
    val defaultSeats =
        Seats(
            listOf(
                seat1,
                seat2,
            ),
        )
    val screen = Screen("1관 테스트", defaultSeats)

    @Test
    fun `예약 가능한 좌석들을 불러올 수 있다`() {
        // given: 기본 screen과 seat1의 예약을 가진 ScreeningSeatMap를 생성
        val reservedSeats = Seats(listOf(seat1))
        val screeningSeatMap = ScreeningSeatMap(screen, reservedSeats)
        val expected = Seats(listOf(seat2))

        // when: 예약 가능한 좌석들을 호출
        val actual = screeningSeatMap.getAvailableSeats()

        // then: 예약 가능한 좌석의 좌석번호들이 같다.
        assertThat(actual.seatNumbers).isEqualTo(expected.seatNumbers)
    }

    @Test
    fun `중복된 좌석을 예약하면 예외가 발생한다`() {
        // given: 기본 screen을 가진 ScreeningSeatMap를 생성하고 중복된 seatNumbers 생성
        val screeningSeatMap = ScreeningSeatMap(screen)
        val seatNumber = seat1.seatNumber
        val seatNumbers = listOf(seatNumber, seatNumber)

        // when & then: 중복된 좌석을 예약한면 예외 발생
        assertThrows(IllegalArgumentException::class.java) {
            screeningSeatMap.reserve(seatNumbers)
        }
    }

    @Test
    fun `중복되지 않은 좌석을 예약하면 예약 가능하다`() {
        // given: 기본 screen을 가진 ScreeningSeatMap를 생성하고 중복된 seatNumbers 생성
        val screeningSeatMap = ScreeningSeatMap(screen)
        val seatNumber1 = seat1.seatNumber
        val seatNumber2 = seat2.seatNumber
        val seatNumbers = listOf(seatNumber1, seatNumber2)

        // when: 중복되지 않은 좌석을 예약
        val actual = screeningSeatMap.reserve(seatNumbers)

        // then: actual은 새로운 ScreeningSeatMap을 반환
        assertThat(actual).isInstanceOf(ScreeningSeatMap::class.java)
    }

    @Test
    fun `이미 예약된 좌석은 예약하면 예외가 발생한다`() {
        // given: 기본 screen과 seat1의 예약을 가진 ScreeningSeatMap를 생성
        val reservedSeats = Seats(listOf(seat1))
        val screeningSeatMap = ScreeningSeatMap(screen, reservedSeats)

        // when & then: seat1 을 예약
        assertThrows(IllegalArgumentException::class.java) {
            screeningSeatMap.reserve(listOf(seat1.seatNumber))
        }
    }

    @Test
    fun `예약되지 않은 좌석을 예약하면 예약 가능하다`() {
        // given: 기본 screen과 seat1의 예약을 가진 ScreeningSeatMap를 생성
        val reservedSeats = Seats(listOf(seat1))
        val screeningSeatMap = ScreeningSeatMap(screen, reservedSeats)

        // when: seat2 을 예약
        val actual = screeningSeatMap.reserve(listOf(seat2.seatNumber))

        // then: actual은 새로운 ScreeningSeatMap을 반환
        assertThat(actual).isInstanceOf(ScreeningSeatMap::class.java)
    }
}
