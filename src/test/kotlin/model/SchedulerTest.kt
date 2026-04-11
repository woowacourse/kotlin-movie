@file:Suppress("NonAsciiCharacters")

package model

import model.movie.Movie
import model.movie.RunningTime
import model.screening.Screening
import model.screening.ScreeningSeatMap
import model.screening.Screenings
import model.seat.SeatNumber
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import repository.ScreenRepository
import repository.ScreeningRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class SchedulerTest {
    private val movie = Movie("테스트", RunningTime(100))
    private val seatMap = ScreeningSeatMap(ScreenRepository.screen1)
    private val date = LocalDate.of(2026, 4, 12)
    private val screening = Screening(movie, LocalDateTime.of(date, LocalTime.of(12, 0)), seatMap)
    private val screeningRepository = ScreeningRepository(Screenings(listOf(screening)))
    private val scheduler = Scheduler(screeningRepository)

    @Test
    fun `특정 영화와 날짜로 상영 일정을 조회할 수 있다`() {
        val screenings = scheduler.findBy(movie, date)

        assertThat(screenings.isEmpty()).isFalse()
        assertThat(screenings.all { it.movie == movie && it.showDate == date }).isTrue()
    }

    @Test
    fun `상영 일정을 업데이트할 수 있다`() {
        // given
        val reservedSeatMap = seatMap.reserve(listOf(SeatNumber('A', 1)))
        val updatedScreening = screening.copy(seatMap = reservedSeatMap)

        // when
        scheduler.update(updatedScreening)

        // then
        val found = scheduler.findBy(movie, date).first()
        assertThat(found.seatMap.getAvailableSeats().seatCount).isNotEqualTo(seatMap.getAvailableSeats().seatCount)
    }
}
