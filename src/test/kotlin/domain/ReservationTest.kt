package domain

import kotlin.time.Duration.Companion.minutes
import kotlinx.datetime.LocalDate
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

    fun findShowing(
        movieTheater: MovieTheater,
        movie: Movie,
        date: LocalDate,
    ): List<Showing> {
        val showings = movieTheater.showings.filter { it.movie == movie && it.startTime.date == date }
        require(showings.isNotEmpty()) { "해당 영화는 해당 날짜에 상영되지 않습니다." }
        return showings
    }

    fun findUser(
        movieTheater: MovieTheater,
        id: Int,
    ): User {
        val userIndex = movieTheater.users.indexOfFirst { it.id == id }

        require(userIndex != -1) { "존재하지 않는 사용자입니다." }
        return movieTheater.users[userIndex]
    }

    fun makeReservation(
        movieTheater: MovieTheater,
        userId: Int,
        showing: Showing,
    ) {
        checkReservationHistory(movieTheater.reservationInfos, userId, showing)
    }

    fun checkReservationHistory(
        reservationInfos: List<ReservationInfo>,
        userId: Int,
        showing: Showing,
    ) {
        val history = reservationInfos.filter {
            it.user.id == userId &&
                (showing.startTime >= it.showing.startTime && showing.startTime <= it.showing.endTime)
        }

        require(history.isEmpty()) { "동일한 시간대에 예매한 내역이 존재합니다." }
    }

    fun checkSeatFormat(seat: String) {
        require(Regex("^[A-Z][0-9]+$").matches(seat)) { "입력된 값이 유효하지 않습니다." }
    }

    fun checkSeat(
        seats: List<Seat>,
        input: String,
    ) {
        checkSeatFormat(input)
        val row = input[0]
        val column = input.substring(1).toInt()

        val seat = seats.filter { it.row == row && it.column == column }
        require(seat.isNotEmpty()) { "해당 상영관에는 해당 좌석이 존재하지 않습니다." }
    }
}

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

    @Test
    fun `영화의 id가 존재할 경우, 해당 영화를 반환한다`() {
        // given : 확인하려는 영화의 id는 1이다.
        val movieId = 1

        // when : 영화를 찾으면
        val result = Reservation.findMovieById(TestFixtureData.movieTheater, movieId)

        // then : 해당 영화가 반환된다.
        assertEquals(TestFixtureData.movies.first(), result)
    }

    @Test
    fun `예매 영화와 예매 날짜를 입력하였을 때 해당하는 상영 일정을 반환한다`() {
        // given : 예매 영화의 id는 1이고, 예매 날짜는 2026-4-10일이다.
        val movieId = 1
        val date = LocalDate(2026, 4, 10)

        // when : 전체 영화 리스트에서 영화를 확인하고, 전체 상영 일정에서 해당 영화와 예매 날짜를 검색하면
        val movie = Reservation.findMovieById(TestFixtureData.movieTheater, movieId)
        val result = Reservation.findShowing(TestFixtureData.movieTheater, movie, date)

        // then : 해당하는 상영 일정을 반환한다.
        assertEquals(listOf<Showing>(TestFixtureData.showings.first()), result)
    }

    @Test
    fun `예매 영화와 예매 날짜를 입력하였을 때 해당하는 상영 일정이 없을 경우, 예외가 발생한다`() {
        // given : 예매 영화의 id는 1이고, 예매 날짜는 2026-4-11일이다.
        val movieId = 1
        val date = LocalDate(2026, 4, 11)

        // when : 전체 영화 리스트에서 영화를 확인하고, 전체 상영 일정에서 해당 영화와 예매 날짜를 검색하면
        val movie = Reservation.findMovieById(TestFixtureData.movieTheater, movieId)
        val exception = assertThrows<IllegalArgumentException> {
            Reservation.findShowing(TestFixtureData.movieTheater, movie, date)
        }
        // then : 예외가 발생한다.
        assertEquals("해당 영화는 해당 날짜에 상영되지 않습니다.", exception.message)
    }

    @Test
    fun `존재하지 않는 사용자를 찾는 경우, 예외가 발생한다`() {
        // given : 사용자의 id 는 4이다.
        val userId = 4

        // when : 사용자의 id에 해당하는 사용자를 찾을 경우
        val exception = assertThrows<IllegalArgumentException> {
            Reservation.findUser(TestFixtureData.movieTheater, userId)
        }
        // then : 예외가 발생한다.
        assertEquals("존재하지 않는 사용자입니다.", exception.message)
    }

    @Test
    fun `동일한 시간대에 이미 예매한 내역이 있으면 예외가 발생한다`() {
        // given : 사용자의 id 는 1이다. 사용자는 해당 시간에 예매한 내역이 있다.
        val userId = 1
        val showing = TestFixtureData.showings[0]

        // when : 사용자가 해당 시간에 예약하려고 할 때
        val exception = assertThrows<IllegalArgumentException> {
            Reservation.checkReservationHistory(TestFixtureData.movieTheater.reservationInfos, userId, showing)
        }
        // then : 예외가 발생한다.
        assertEquals("동일한 시간대에 예매한 내역이 존재합니다.", exception.message)
    }

    @Test
    fun `좌석 정보가 (알파벳)(숫자) 형식이 아니면 예외가 발생한다`() {
        // given : 입력값이 11이다.
        val seat = "11"

        // when : 사용자가 해당 좌석을 예약하려고 할 때
        val exception = assertThrows<IllegalArgumentException> {
            Reservation.checkSeatFormat(seat)
        }
        // then : 예외가 발생한다.
        assertEquals("입력된 값이 유효하지 않습니다.", exception.message)
    }

    @Test
    fun `해당 상영관에 존재하지 않는 좌석이면 예외가 발생한다`() {
        // given : 입력값이 D1이다.
        val seat = "D1"

        // when : 사용자가 해당 좌석을 예약하려고 할 때
        val exception = assertThrows<IllegalArgumentException> {
            Reservation.checkSeat(TestFixtureData.seats, seat)
        }
        // then : 예외가 발생한다.
        assertEquals("해당 상영관에는 해당 좌석이 존재하지 않습니다.", exception.message)
    }
}

class Movie(val title: String, val id: Int, val runningTime: Int)

class MovieTheater(
    val screens: List<Screen>,
    val movies: List<Movie>,
    val showings: List<Showing>,
    val reservationInfos: List<ReservationInfo>,
    val users: List<User>,
)

class Screen(val seats: List<Seat>, val id: Int)

class Showing(val startTime: LocalDateTime, val screen: Screen, val movie: Movie) {
    val endTime = startTime
        .toInstant(TimeZone.currentSystemDefault())
        .plus(movie.runningTime.minutes)
        .toLocalDateTime(TimeZone.currentSystemDefault())
}

class ReservationInfo(val showing: Showing, val seat: Seat, val user: User)
