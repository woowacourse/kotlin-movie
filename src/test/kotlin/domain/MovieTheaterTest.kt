package domain

import domain.cinema.ScreeningSchedule
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class MovieTheaterTest {
    @Test
    fun `영화의 id가 존재하지 않으면 null을 반환한다`() {
        val movieId = Id("movie-unknown")

        val result = TestFixtureData.movieTheater.findMovieById(movieId)

        assertNull(result)
    }

    @Test
    fun `영화의 id가 존재할 경우 해당 영화를 반환한다`() {
        val movieId = Id("movie-harry-potter")

        val result = TestFixtureData.movieTheater.findMovieById(movieId)

        assertEquals(TestFixtureData.movies.first(), result)
    }

    @Test
    fun `예매 영화와 예매 날짜를 입력하면 해당하는 상영 일정을 반환한다`() {
        val movieId = Id("movie-harry-potter")
        val date = LocalDate(2026, 4, 10)

        val movie = TestFixtureData.movieTheater.findMovieById(movieId)
        val result = movie?.let { TestFixtureData.movieTheater.findScreenings(it, date) }

        assertEquals(listOf(TestFixtureData.screenings.first()), result)
    }

    @Test
    fun `예매 영화와 예매 날짜를 입력했을 때 해당하는 상영 일정이 없으면 빈 리스트를 반환한다`() {
        val movieId = Id("movie-harry-potter")
        val date = LocalDate(2026, 4, 11)

        val movie = TestFixtureData.movieTheater.findMovieById(movieId)
        val result = movie?.let { TestFixtureData.movieTheater.findScreenings(it, date) }

        assertEquals(emptyList<ScreeningSchedule>(), result)
    }
}
