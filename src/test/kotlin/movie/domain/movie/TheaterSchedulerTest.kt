package movie.domain.movie

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class TheaterSchedulerTest {
    @Test
    fun `상영중인 영화 제목을 반환한다`() {
        val openTime = LocalTime.of(0, 0, 0)
        val closeTime = LocalTime.of(23, 59, 59)
        val theater = Theater(openTime = openTime, closeTime = closeTime)
        val movieTitle = MovieTitle("아이언맨")
        val movie = Movie(title = movieTitle)
        val screeningMovie = ScreeningMovie(
            theater = theater,
            movie = movie,
            movieTime = MovieTime(
                date = LocalDate.of(2026, 4, 8),
                startTime = LocalTime.of(1, 1, 1),
                endTime = LocalTime.of(2, 1, 1),
            )
        )
        val theaterScheduler = TheaterScheduler(
            theaters = listOf(theater),
            screeningMovies = listOf(screeningMovie)
        )

        assertThat(theaterScheduler.getMovieTitles()).isEqualTo(listOf(movieTitle))
    }

    @Test
    fun `시간이 겹치지 않는다면 영화가 추가된다`() {
        val openTime = LocalTime.of(0, 0, 0)
        val closeTime = LocalTime.of(23, 59, 59)
        val theater = Theater(openTime = openTime, closeTime = closeTime)
        val movieTitle = MovieTitle("아이언맨")
        val movie = Movie(title = movieTitle)
        val movieTime = MovieTime(
            date = LocalDate.of(2026, 4, 8),
            startTime = LocalTime.of(1, 1, 1),
            endTime = LocalTime.of(2, 1, 1),
        )
        val theaterScheduler = TheaterScheduler(theaters = listOf(theater))

        theaterScheduler.addMovie(
            theaterId = theater.id,
            movieTime = movieTime,
            movie = movie
        )

        assertThat(theaterScheduler.getMovieTitles()).isEqualTo(listOf(movieTitle))
    }

    @Test
    fun `시간이 겹친다면 true를 반환한다`() {
        val openTime = LocalTime.of(0, 0, 0)
        val closeTime = LocalTime.of(23, 59, 59)
        val theater = Theater(openTime = openTime, closeTime = closeTime)
        val movie = Movie(title = MovieTitle("아이언맨"))
        val movieTime = MovieTime(
            date = LocalDate.of(2026, 4, 8),
            startTime = LocalTime.of(1, 1, 1),
            endTime = LocalTime.of(2, 1, 1),
        )
        val screeningMovie = ScreeningMovie(
            theater = theater,
            movie = movie,
            movieTime = movieTime
        )

        val theaterScheduler = TheaterScheduler(
            theaters = listOf(theater),
            screeningMovies = listOf(screeningMovie)
        )

        val isDupTime = theaterScheduler.checkDuplicateTime(
            theaterId = theater.id,
            movieTime = movieTime
        )

        assertThat(isDupTime).isTrue
    }

    @Test
    fun `시간이 겹치지 않는다면 false를 반환한다`() {
        val openTime = LocalTime.of(0, 0, 0)
        val closeTime = LocalTime.of(23, 59, 59)
        val theater = Theater(openTime = openTime, closeTime = closeTime)

        val movieTime = MovieTime(
            date = LocalDate.of(2026, 4, 8),
            startTime = LocalTime.of(1, 1, 1),
            endTime = LocalTime.of(2, 1, 1),
        )
        val theaterScheduler = TheaterScheduler(theaters = listOf(theater))

        val isDupTime = theaterScheduler.checkDuplicateTime(
            theaterId = theater.id,
            movieTime = movieTime
        )

        assertThat(isDupTime).isFalse
    }

    @Test
    fun `특정 영화의 상영시간을 반환한다`() {
        val openTime = LocalTime.of(0, 0, 0)
        val closeTime = LocalTime.of(23, 59, 59)
        val theater = Theater(openTime = openTime, closeTime = closeTime)
        val movie = Movie(title = MovieTitle("아이언맨"))
        val movieTime = MovieTime(
            date = LocalDate.of(2026, 4, 8),
            startTime = LocalTime.of(1, 1, 1),
            endTime = LocalTime.of(2, 1, 1),
        )
        val screeningMovie = ScreeningMovie(
            theater = theater,
            movie = movie,
            movieTime = movieTime
        )

        val theaterScheduler = TheaterScheduler(
            theaters = listOf(theater),
            screeningMovies = listOf(screeningMovie)
        )

        val movieTimes = theaterScheduler.getMovieTimes(movie.id)

        assertThat(movieTimes.size).isEqualTo(1)
    }
}