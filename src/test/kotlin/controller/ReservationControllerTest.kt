package controller

import domain.Movie
import domain.MovieTheater
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

    fun chooseDate(): LocalDate {
        println("날짜를 입력하세요 (YYYY-MM-DD):")
        val input = readln()

        try {
            return LocalDate.parse(input)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("올바른 날짜 형식이 아닙니다. (YYYY-MM-DD)")
        }
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
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 날짜를 처리하면
        val exception = assertThrows<IllegalArgumentException> {
            controller.chooseDate()
        }

        // then : 예외가 발생한다.
        assertEquals("올바른 날짜 형식이 아닙니다. (YYYY-MM-DD)", exception.message)
    }
}
