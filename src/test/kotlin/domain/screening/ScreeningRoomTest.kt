package domain.screening

import domain.common.TimeRange
import domain.DomainTestFixture.createScreeningRoom
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalTime

class ScreeningRoomTest {
    @Test
    fun `이름, 운영시간, 좌석 목록을 가진다`() {
        // given
        val name = ScreeningRoomName("커피")
        val operatingTime = TimeRange(LocalTime.of(10, 0), LocalTime.of(18, 0))
        val seats = createScreeningRoom().seats
        val screeningRoom = ScreeningRoom(name = name, operatingTime = operatingTime, seats = seats)

        // when & then
        screeningRoom.name shouldBe name
        screeningRoom.operatingTime shouldBe operatingTime
        screeningRoom.seats shouldBe seats
    }

    @Test
    fun `이름이 공백일 경우 예외를 던진다`() {
        // when & then
        shouldThrow<IllegalArgumentException> {
            createScreeningRoom(name = " ")
        }
    }

    @Test
    fun `좌석이 없을 경우 예외를 던진다`() {
        // when & then
        shouldThrow<IllegalArgumentException> {
            createScreeningRoom(seats = emptyList())
        }
    }
}
