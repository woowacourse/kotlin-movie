package domain

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalTime

class ScreeningRoomTest {
    @Test
    fun `이름, 운영시간, 좌석 목록을 가진다`() {
        ScreeningRoom(
            name = "챱",
            startTime = LocalTime.of(10, 0),
            endTime = LocalTime.of(18, 0),
            seats = listOf(Seat(Row("A"), Column(1))),
        )
    }

    @Test
    fun `이름이 공백일 경우 예외를 던진다`() {
        assertThrows(IllegalArgumentException::class.java) {
            ScreeningRoom(
                name = " ",
                startTime = LocalTime.of(10, 0),
                endTime = LocalTime.of(18, 0),
                seats = listOf(Seat(Row("A"), Column(1))),
            )
        }
    }

    @Test
    fun `운영 종료 시간이 시작 시간보다 앞설 경우 예외를 던진다`() {
        assertThrows(IllegalArgumentException::class.java) {
            ScreeningRoom(
                name = " 커비",
                startTime = LocalTime.of(10, 0),
                endTime = LocalTime.of(9, 0),
                seats = listOf(Seat(Row("A"), Column(1))),
            )
        }
    }

    @Test
    fun `좌석이 없을 경우 예외를 던진다`() {
        assertThrows(IllegalArgumentException::class.java) {
            ScreeningRoom(
                name = " 커비",
                startTime = LocalTime.of(10, 0),
                endTime = LocalTime.of(9, 0),
                seats = emptyList(),
            )
        }
    }
}
