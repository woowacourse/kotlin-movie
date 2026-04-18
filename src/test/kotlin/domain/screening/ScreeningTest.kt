package domain.screening

import domain.DomainTestFixture.createMovie
import domain.DomainTestFixture.createScreening
import domain.DomainTestFixture.createScreeningRoom
import domain.DomainTestFixture.seatA1
import domain.common.TimeRange
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.LocalTime

class ScreeningTest {
    @Test
    fun `상영은 영화, 상영관, 상영 날짜 및 시간을 가진다`() {

        // given
        val movie = createMovie()
        val room = createScreeningRoom()
        val startTime = LocalDateTime.of(2026, 4, 8, 10, 0)

        // when
        val screening = createScreening(movie = movie, room = room, startTime = startTime)

        // then
        screening.movie shouldBe movie
        screening.room shouldBe room
        screening.startTime shouldBe startTime
    }

    @Test
    fun `상영 시작 시간과 영화 상영 길이에 따른 시작 및 종료 시간을 가진다`() {

        // given
        val movie = createMovie(runningTime = 167)
        val startTime = LocalDateTime.of(2026, 4, 8, 10, 0)
        val screening = createScreening(movie = movie, startTime = startTime)

        // when & then
        screening.screenTimeRange.start shouldBe LocalTime.of(10, 0)
        screening.screenTimeRange.end shouldBe LocalTime.of(12, 47)
    }

    @Test
    fun `상영 시간이 상영관의 운영시간에 포함되지 않을 경우 예외를 던진다`() {

        // given
        val room = createScreeningRoom(
            operatingTime = TimeRange(LocalTime.of(10, 0), LocalTime.of(18, 0))
        )
        val outOfRangeTime = LocalDateTime.of(2026, 4, 8, 9, 0)

        // when & then
        shouldThrow<IllegalArgumentException> {
            createScreening(room = room, startTime = outOfRangeTime)
        }
    }

    @Test
    fun `좌석 예약 시 해당 좌석이 예약된 새로운 Screening 객체를 반환한다`() {

        // given
        val screening = createScreening()
        val position = seatA1()

        // when
        val reservedScreening = screening.reserve(position)

        // then
        screening.seats.isReservable(position) shouldBe true
        reservedScreening.seats.isReservable(position) shouldBe false

    }

    @Test
    fun `상영 시간이 겹치는지 확인할 수 있다`() {

        // given
        val startTime = LocalDateTime.of(2026, 4, 10, 10, 0)
        val s1 = createScreening(startTime = startTime)
        val s2 = createScreening(startTime = startTime.plusMinutes(30))

        // when & then
        s1.isOverlapping(s2) shouldBe true
    }

    @Test
    fun `동일한 ID를 가진 상영인지 확인할 수 있다`() {

        // given
        val s1 = createScreening()
        val s2 = s1.reserve(seatA1())

        // when & then
        s1.isSame(s2) shouldBe true
        s1.id shouldBe s2.id
    }

}
