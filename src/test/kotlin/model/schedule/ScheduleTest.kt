package model.schedule

import model.movie.Movie
import model.seat.SeatInventory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ScheduleTest {
    @Test
    fun `일정은 시작 시각과 종료 시각을 가지고 있다`() {
        val openTime = LocalTime.of(9, 0)
        val closeTime = LocalTime.of(23, 0)

        val schedule = createSchedule(openTime = openTime, closeTime = closeTime)

        assertThat(schedule.openTime).isEqualTo(openTime)
        assertThat(schedule.closeTime).isEqualTo(closeTime)
    }

    @Test
    fun `일정의 총 시각(시작 시각부터 종료 시각까지)은 영화의 상영 시간보다 크거나 같아야 한다`() {
        val movieRunningTime = Duration.ofMinutes(130)

        val schedule = createSchedule()

        val totalTime = Duration.between(schedule.openTime, schedule.closeTime)
        assertThat(totalTime).isGreaterThanOrEqualTo(movieRunningTime)
    }

    @Test
    fun `일정은 좌석들을 갖고 있다`() {
        val schedule = createSchedule()

        assertThat(
            schedule.screenings
                .first()
                .seatInventory.seats,
        ).isNotEmpty
    }

    private fun createSchedule(
        openTime: LocalTime = LocalTime.of(9, 0),
        closeTime: LocalTime = LocalTime.of(23, 0),
        screenings: List<Screening> = listOf(createScreening()),
    ): Schedule = Schedule(openTime, closeTime, screenings)

    private fun createScreening(startDateTime: LocalDateTime = LocalDateTime.of(2026, 4, 10, 10, 0)): Screening =
        Screening(
            movie =
                Movie(
                    movieTitle = "인터스텔라",
                    movieRunningTime = 130,
                    startDate = LocalDate.of(2026, 4, 1),
                    endDate = LocalDate.of(2026, 4, 30),
                ),
            startDateTime = startDateTime,
            seatInventory = SeatInventory.createDefaultSeatInventory(),
        )
}
