package domain

import kotlin.time.Duration.Companion.minutes
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

object Reservation {
    fun findMovieById(
        movieTheater: MovieTheater,
        id: Int,
    ): Movie {
        val movieIndex = movieTheater.movies.indexOfFirst { it.id == id }
        require(movieIndex != -1) { "해당 영화는 존재하지 않습니다." }
        return movieTheater.movies[movieIndex]
    }
}

class Movie(val title: String, val id: Int, val runningTime: Int)

class MovieTheater(val screens: List<Screen>, val movies: List<Movie>, val reservationInfos: List<ReservationInfo>, val users: List<User>)

class Screen(val seats: List<Seat>, val id: Int)

class Showing(val startTime: LocalDateTime, val screen: Screen, val movie: Movie) {
    val endTime = startTime
        .toInstant(TimeZone.currentSystemDefault())
        .plus(movie.runningTime.minutes)
        .toLocalDateTime(TimeZone.currentSystemDefault())
}

class ReservationInfo(val showing: Showing, val seat: Seat, val user: User)

class ReservationTest {
    @Test
    fun `영화의 id가 존재하지 않으면 예외가 발생한다`() {
        // given : 확인하려는 영화의 id는 10이다.(존재하지 않는다.)
        val movieId = 10

        // when : 전체 영화 리스트에서 영화를 확인하면
        val exception = assertThrows<IllegalArgumentException> {
            Reservation.findMovieById(TestFixtureData.movieTheater, movieId)
        }
        // then : 예외가 발생한다.
        assertEquals("해당 영화는 존재하지 않습니다.", exception.message)
    }
}
