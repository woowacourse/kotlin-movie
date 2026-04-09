package controller

import domain.Movie
import domain.MovieTheater
import domain.Reservation
import domain.Showing
import java.io.ByteArrayInputStream
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ReservationController(val movieTheater: MovieTheater) {
    fun run() {
    }

    fun create(): Boolean {
        println("영화 예매를 시작합니다. 새 예매를 생성하시겠습니까? (Y/N)")
        val input = readln()

        require(input == "Y" || input == "N") { "입력값은 Y 혹은 N이어야 합니다." }

        when (input) {
            "Y" -> return true
        }
        return false
    }

    fun chooseMovie(): Movie {
        println("예매할 영화 제목을 입력하세요:")
        val input = readln()

        val movieIndex = movieTheater.movies.indexOfFirst { it.title == input }

        require(movieIndex != -1) { "존재하지 않는 영화입니다." }

        return movieTheater.movies[movieIndex]
    }

    fun chooseDate(movie: Movie): LocalDate {
        println("날짜를 입력하세요 (YYYY-MM-DD):")
        val input = readln()

        val date = runCatching { LocalDate.parse(input) }.getOrNull()
        require(date != null) { "올바른 날짜 형식이 아닙니다. (YYYY-MM-DD)" }

        val showings = movieTheater.showings.filter { it.movie == movie && it.startTime.date == date }
        require(showings.isNotEmpty()) { "해당 날짜에 선택한 영화의 상영이 없습니다." }

        return date
    }

    fun chooseShowingTime(
        movie: Movie,
        date: LocalDate,
    ): Showing {
        val showings = movieTheater.showings.filter { it.movie == movie && it.startTime.date == date }
        println("해당 날짜의 상영 목록")

        showings.forEachIndexed { index, showing ->
            println("[${index + 1}] ${showing.startTime.time}")
        }
        println("상영 번호를 선택하세요:")
        val input = readln()

        require(input.toIntOrNull() != null && input.toInt() <= showings.size) { "선택하신 상영 번호는 없는 상영 번호입니다." }

        Reservation.checkReservationHistory(movieTheater.reservationInfos, showings[input.toInt() - 1])

        return showings[input.toInt() - 1]
    }
}

class ReservationControllerTest {

    val controller = ReservationController(TestFixtureData.movieTheater)

    @Test
    fun `생성 여부가 Y,N이 아닐 경우 예외가 발생한다`() {
        // given : 생성 여부가 X로 입력된다.
        val input = "X"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 입력 여부를 처리하면
        val exception = assertThrows<IllegalArgumentException> {
            controller.create()
        }

        // then : 예외가 발생한다.
        assertEquals("입력값은 Y 혹은 N이어야 합니다.", exception.message)
    }

    @Test
    fun `생성 여부가 Y일 경우 true를 반환한다`() {
        // given : 생성 여부가 Y로 입력된다.
        val input = "Y"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 입력 여부를 처리하면
        val result = controller.create()

        // then : true가 반환된다.
        assertEquals(true, result)
    }

    @Test
    fun `존재하지 않는 영화 제목이면 예외가 발생한다`() {
        // given : 존재하지 않는 영화 제목이 입력된다.
        val input = "X"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 영화 제목을 처리하면
        val exception = assertThrows<IllegalArgumentException> {
            controller.chooseMovie()
        }

        // then : 예외가 발생한다.
        assertEquals("존재하지 않는 영화입니다.", exception.message)
    }

    @Test
    fun `존재하는 영화 제목이면 해당 영화를 반환한다`() {
        // given : 존재하는 영화 제목이 입력된다.
        val input = "해리 포터"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 영화 제목을 처리하면
        val result = controller.chooseMovie()

        // then : 영화가 반환된다.
        assertEquals(TestFixtureData.movieTheater.movies.first(), result)
    }

    @Test
    fun `날짜 형식이 YYYY-MM-DD가 아니면 예외가 발생한다`() {
        // given : 2026_04_08이 입력된다.
        val input = "2026_04_08"
        val movie: Movie = TestFixtureData.movieTheater.movies.first()
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 날짜를 처리하면
        val exception = assertThrows<IllegalArgumentException> {
            controller.chooseDate(movie)
        }

        // then : 예외가 발생한다.
        assertEquals("올바른 날짜 형식이 아닙니다. (YYYY-MM-DD)", exception.message)
    }

    @Test
    fun `날짜 형식이 YYYY-MM-DD이면 Date를 반환한다`() {
        // given : 2026-04-10이 입력된다.
        val input = "2026-04-10"
        val movie: Movie = TestFixtureData.movieTheater.movies.first()
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 날짜를 처리하면
        val result = controller.chooseDate(movie)

        // then : Date가 반환된다.
        assertEquals(LocalDate(2026, 4, 10), result)
    }

    @Test
    fun `해당 날짜에 선택한 영화의 상영이 없으면 예외가 발생한다`() {
        // given : 특정 영화를 선택한 상태에서 상영일자가 존재하지 않는 날짜를 입력한다
        val input = "2026-04-08"
        val movie: Movie = TestFixtureData.movieTheater.movies.first()

        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 상영을 확인하면
        val exception = assertThrows<IllegalArgumentException> {
            controller.chooseDate(movie)
        }

        // then : 예외가 발생한다.
        assertEquals("해당 날짜에 선택한 영화의 상영이 없습니다.", exception.message)
    }

    @Test
    fun `해당 날짜에 상영이 있으면 해당 상영을 반환한다`() {
        // given : 특정 영화를 선택한 상태에서 상영일자가 존재하는 날짜를 입력한다
        val movie: Movie = TestFixtureData.movieTheater.movies[2]
        val date = LocalDate(2026, 4, 10)

        // when : 상영을 처리하고 1번을 입력하면
        val input = "1"
        System.setIn(ByteArrayInputStream(input.toByteArray()))
        val result = controller.chooseShowingTime(movie, date)

        // then : 특정 상영이 반환된다.
        assertEquals(TestFixtureData.movieTheater.showings[2], result)
    }

    @Test
    fun `상영 번호가 유효 범위 밖이면 예외가 발생한다`() {
        // given : 상영번호에 대해 유효 범위 밖 값을 입력한다
        val input = "2"
        System.setIn(ByteArrayInputStream(input.toByteArray()))
        val movie: Movie = TestFixtureData.movieTheater.movies.first()
        val date = LocalDate(2026, 4, 10)

        // when : 상영을 확인한 뒤 상영 번호를 입력하면
        val exception = assertThrows<IllegalArgumentException> {
            controller.chooseShowingTime(movie, date)
        }

        // then : 예외가 발생한다.
        assertEquals("선택하신 상영 번호는 없는 상영 번호입니다.", exception.message)
    }

    @Test
    fun `선택한 상영 시간이 장바구니의 기존 예매와 겹치면 예외가 발생한다`() {
        // given : 상영번호에 대해 시간이 겹치는 시간대를 예매한다.
        val input = "1"
        System.setIn(ByteArrayInputStream(input.toByteArray()))
        val movie: Movie = TestFixtureData.movieTheater.movies.first()
        val date = LocalDate(2026, 4, 10)

        // when : 상영을 확인한 뒤 상영 번호를 입력하면
        val exception = assertThrows<IllegalArgumentException> {
            controller.chooseShowingTime(movie, date)
        }

        // then : 예외가 발생한다.
        assertEquals("선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요.", exception.message)
    }
}
