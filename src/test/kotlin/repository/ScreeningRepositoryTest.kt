@file:Suppress("NonAsciiCharacters")

package repository

import model.movie.Movie
import model.movie.RunningTime
import model.seat.SeatNumber
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import repository.MovieRepository.F1_THE_MOVIE
import repository.MovieRepository.IRON_MAN
import java.time.LocalDate

class ScreeningRepositoryTest {
    private val baseDate: LocalDate = LocalDate.of(2025, 9, 20)

    @Test
    fun `특정 영화와 날짜에 해당하는 상영 정보를 찾을 수 있다`() {
        // given
        val repository = ScreeningRepository()

        // when
        val screenings = repository.findBy(F1_THE_MOVIE, baseDate)

        // then
        assertThat(screenings.isEmpty()).isFalse()
        assertThat(screenings.all { it.movie == F1_THE_MOVIE && it.showDate == baseDate }).isTrue()
    }

    @Test
    fun `해당하는 상영 정보가 없으면 빈 결과를 반환한다`() {
        // given
        val repository = ScreeningRepository()
        val otherMovie = Movie("없는 영화", RunningTime(100))
        val otherDate = LocalDate.of(2100, 1, 1)

        // when
        val screeningsByMovie = repository.findBy(otherMovie, baseDate)
        val screeningsByDate = repository.findBy(F1_THE_MOVIE, otherDate)

        // then
        assertThat(screeningsByMovie.isEmpty()).isTrue()
        assertThat(screeningsByDate.isEmpty()).isTrue()
    }

    @Test
    fun `상영 정보를 업데이트할 수 있다`() {
        // given
        val repository = ScreeningRepository()
        val targetScreening = repository.findBy(IRON_MAN, baseDate).first()
        val seatNumber = SeatNumber('A', 1)

        // 좌석 하나를 예약한 새로운 상태의 Screening 생성
        val updatedScreening = targetScreening.reserve(listOf(seatNumber)).screening

        // when
        repository.update(updatedScreening)

        // then
        val found = repository.findBy(IRON_MAN, baseDate).first()
        assertThat(found.seatMap.getAvailableSeats().seatNumbers).doesNotContain(seatNumber)
    }

    @Test
    fun `업데이트 시 다른 상영 정보는 영향을 받지 않는다`() {
        // given
        val repository = ScreeningRepository()
        val fMovieScreenings = repository.findBy(F1_THE_MOVIE, baseDate)
        val targetScreening = fMovieScreenings.first()
        val otherScreeningBefore = fMovieScreenings.toList()[1]

        val updatedScreening = targetScreening.reserve(listOf(SeatNumber('A', 1))).screening

        // when
        repository.update(updatedScreening)

        // then
        val otherScreeningAfter = repository.findBy(F1_THE_MOVIE, baseDate).toList()[1]
        assertThat(otherScreeningAfter.seatMap.getAvailableSeats().seatCount)
            .isEqualTo(otherScreeningBefore.seatMap.getAvailableSeats().seatCount)
    }
}
