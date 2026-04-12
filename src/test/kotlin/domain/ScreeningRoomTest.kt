package domain

import domain.screening.ScreeningRoom
import domain.screening.ScreeningRoomName
import domain.screening.TimeRange
import domain.seat.Column
import domain.seat.Row
import domain.seat.Seat
import domain.seat.SeatPosition
import domain.seat.Seats
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalTime

class ScreeningRoomTest {
    @Test
    fun `이름, 운영시간, 좌석 목록을 가진다`() {
        ScreeningRoom(
            name = ScreeningRoomName("커피"),
            operatingTime = TimeRange(LocalTime.of(10, 0), LocalTime.of(18, 0)),
            seats = Seats(listOf(Seat(SeatPosition(Row.A, Column(1))))),
        )
    }

    @Test
    fun `이름이 공백일 경우 예외를 던진다`() {
        assertThrows(IllegalArgumentException::class.java) {
            ScreeningRoom(
                name = ScreeningRoomName(" "),
                operatingTime = TimeRange(LocalTime.of(10, 0), LocalTime.of(18, 0)),
                seats = Seats(listOf(Seat(SeatPosition(Row.A, Column(1))))),
            )
        }
    }

    @Test
    fun `좌석이 없을 경우 예외를 던진다`() {
        assertThrows(IllegalArgumentException::class.java) {
            ScreeningRoom(
                name = ScreeningRoomName("커피"),
                operatingTime = TimeRange(LocalTime.of(10, 0), LocalTime.of(18, 0)),
                seats = Seats(emptyList()),
            )
        }
    }
}
