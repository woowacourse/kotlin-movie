@file:Suppress("NonAsciiCharacters")

package movie.repository

import movie.model.movie.Movie
import movie.model.movie.RunningTime
import movie.model.seat.SeatNumber
import movie.repository.inmemory.InMemoryMovieRepository
import movie.repository.inmemory.InMemoryScreeningRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

class InMemoryScreeningRepositoryTest {
    private val baseDate: LocalDate = LocalDate.of(2025, 9, 20)

    @Test
    fun `특정 영화와 날짜에 해당하는 상영 정보를 찾을 수 있다`() {
        // given
        val repository = InMemoryScreeningRepository()

        // when
        val screenings = repository.findBy(InMemoryMovieRepository.F1_THE_MOVIE, baseDate)

        // then
        Assertions.assertThat(screenings.isEmpty()).isFalse()
        Assertions
            .assertThat(screenings.all { it.movie == InMemoryMovieRepository.F1_THE_MOVIE && it.showDate == baseDate })
            .isTrue()
    }

    @Test
    fun `해당하는 상영 정보가 없으면 빈 결과를 반환한다`() {
        // given
        val repository = InMemoryScreeningRepository()
        val otherMovie = Movie(title = "없는 영화", runningTime = RunningTime(100))
        val otherDate = LocalDate.of(2100, 1, 1)

        // when
        val screeningsByMovie = repository.findBy(otherMovie, baseDate)
        val screeningsByDate = repository.findBy(InMemoryMovieRepository.F1_THE_MOVIE, otherDate)

        // then
        Assertions.assertThat(screeningsByMovie.isEmpty()).isTrue()
        Assertions.assertThat(screeningsByDate.isEmpty()).isTrue()
    }

    @Test
    fun `상영 정보를 업데이트할 수 있다`() {
        // given
        val repository = InMemoryScreeningRepository()
        val targetScreening = repository.findBy(InMemoryMovieRepository.IRON_MAN, baseDate).first()
        val seatNumber = SeatNumber('A', 1)

        // 좌석 하나를 예약한 새로운 상태의 Screening 생성
        val updatedScreening = targetScreening.reserve(listOf(seatNumber)).screening

        // when
        repository.update(updatedScreening)

        // then
        val found = repository.findBy(InMemoryMovieRepository.IRON_MAN, baseDate).first()
        Assertions.assertThat(found.seatMap.getAvailableSeats().seatNumbers).doesNotContain(seatNumber)
    }

    @Test
    fun `업데이트 시 다른 상영 정보는 영향을 받지 않는다`() {
        // given
        val repository = InMemoryScreeningRepository()
        val fMovieScreenings = repository.findBy(InMemoryMovieRepository.F1_THE_MOVIE, baseDate)
        val targetScreening = fMovieScreenings.first()
        val otherScreeningBefore = fMovieScreenings.toList()[1]

        val updatedScreening = targetScreening.reserve(listOf(SeatNumber('A', 1))).screening

        // when
        repository.update(updatedScreening)

        // then
        val otherScreeningAfter = repository.findBy(InMemoryMovieRepository.F1_THE_MOVIE, baseDate).toList()[1]
        Assertions
            .assertThat(otherScreeningAfter.seatMap.getAvailableSeats().seatCount)
            .isEqualTo(otherScreeningBefore.seatMap.getAvailableSeats().seatCount)
    }
}
