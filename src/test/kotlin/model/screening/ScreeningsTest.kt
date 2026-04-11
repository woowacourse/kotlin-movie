@file:Suppress("NonAsciiCharacters")

package model.screening

import model.movie.Movie
import model.movie.RunningTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import repository.ScreenRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ScreeningsTest {
    private val movie = Movie("테스트", RunningTime(100))
    private val seatMap = ScreeningSeatMap(ScreenRepository.screen1)
    private val date = LocalDate.of(2026, 4, 12)
    private val screening1 = Screening(movie, LocalDateTime.of(date, LocalTime.of(12, 0)), seatMap)
    private val screening2 = Screening(movie, LocalDateTime.of(date, LocalTime.of(15, 0)), seatMap)

    @Test
    fun `빈 리스트로 생성하면 isEmpty 가 true 를 반환한다`() {
        val screenings = Screenings(emptyList())
        assertThat(screenings.isEmpty()).isTrue()
    }

    @Test
    fun `하나 이상의 상영 정보가 포함되면 isEmpty 가 false 를 반환한다`() {
        val screenings = Screenings(listOf(screening1))
        assertThat(screenings.isEmpty()).isFalse()
    }

    @Test
    fun `반복문을 통해 내부의 상영 정보를 순회할 수 있다`() {
        val screenings = Screenings(listOf(screening1, screening2))
        val list = mutableListOf<Screening>()
        for (screening in screenings) {
            list.add(screening)
        }
        assertThat(list).hasSize(2)
        assertThat(list).containsExactly(screening1, screening2)
    }
}
