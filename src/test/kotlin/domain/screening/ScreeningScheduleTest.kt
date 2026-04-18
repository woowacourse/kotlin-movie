package domain.screening

import domain.DomainTestFixture.createMovie
import domain.DomainTestFixture.createScreening
import domain.DomainTestFixture.createScreeningRoom
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ScreeningScheduleTest {
    @Test
    fun `상영 정보 리스트를 가진다`() {

        val screenings = listOf(createScreening())

        val schedule = ScreeningSchedule(screenings)

        schedule.screenings shouldBe screenings
    }

    @Test
    fun `한 영화가 동시에 상영될 경우 예외를 던진다`() {

        // given
        val startTime = LocalDateTime.of(2026, 4, 8, 10, 0)
        val movie = createMovie(title = "허닛")

        // when
        val screening1 = createScreening(
            id = 1L,
            movie = movie,
            startTime = startTime,
            room = createScreeningRoom(name = "커브볼 1관")
        )
        val screening2 = createScreening(
            id = 2L,
            movie = movie,
            startTime = startTime,
            room = createScreeningRoom(name = "커브볼 2관")
        )

        // then
        shouldThrow<IllegalArgumentException> {
            ScreeningSchedule(listOf(screening1, screening2))
        }
    }

    @Test
    fun `예약 시 좌석 정보가 반영된 새로운 상영 일정이 반환된다`() {
        // given
        val screening = createScreening()
        val schedule = ScreeningSchedule(listOf(screening))
        val ticket = domain.ticket.Ticket(screening, domain.seat.SeatPositions(listOf(domain.DomainTestFixture.seatA1())))
        val bucket = domain.ticket.TicketBucket(listOf(ticket))

        // when
        val updatedSchedule = schedule.reserve(bucket)

        // then
        schedule.screenings[0].seats.isReservable(domain.DomainTestFixture.seatA1()) shouldBe true
        updatedSchedule.screenings[0].seats.isReservable(domain.DomainTestFixture.seatA1()) shouldBe false
    }
}
