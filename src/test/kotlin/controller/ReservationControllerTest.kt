package controller

import java.io.ByteArrayInputStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ReservationControllerTest {

    val controller = ReservationController(TestFixtureData.movieTheater)

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
        assertEquals(TestFixtureData.movieTheater.movies.movies.first(), result)
    }

    @Test
    fun `좌석 입력 형식이 올바르지 않으면 예외가 발생한다`() {
        // given : 좌석을 입력받는다.
        val input = "F//10"
        System.setIn(ByteArrayInputStream(input.toByteArray()))
        val showing = TestFixtureData.showings.first()

        // when : 상영을 확인한 뒤 상영 번호를 입력하면
        val exception = assertThrows<IllegalArgumentException> {
            TestFixtureData.screens.first().seats.checkSeat(input)
        }

        // then : 예외가 발생한다.
        assertEquals("입력된 값이 유효하지 않습니다.", exception.message)
    }

    @Test
    fun `상영관에 존재하지 않는 좌석이면 예외가 발생한다`() {
        // given : 좌석을 입력받는다.
        val input = "F10"
        System.setIn(ByteArrayInputStream(input.toByteArray()))
        val showing = TestFixtureData.showings.first()

        // when : 좌석을 확인한 뒤 좌석 번호를 입력하면
        val exception = assertThrows<IllegalArgumentException> {
            controller.chooseSeat(showing)
        }

        // then : 예외가 발생한다.
        assertEquals("해당 상영관에는 해당 좌석이 존재하지 않습니다.", exception.message)
    }

    @Test
    fun `쉼표로 구분된 여러 좌석을 파싱하면 좌석 리스트를 반환한다`() {
        // given : ,로 구분된 좌석 번호들을 입력한다
        val input = "B1,B2"
        System.setIn(ByteArrayInputStream(input.toByteArray()))
        val showing = TestFixtureData.showings.first()

        // when : 좌석을 확인한 뒤 좌석 번호를 입력하면
        val result = controller.chooseSeat(showing)

        // then : 좌석 리스트가 반환된다.
        assertEquals(listOf(TestFixtureData.seats.seats[2], TestFixtureData.seats.seats[3]), result.seats)
    }

    @Test
    fun `이미 예약된 좌석이면 예외가 발생한다`() {
        // given : 좌석을 입력받는다.
        val input = "A1"
        System.setIn(ByteArrayInputStream(input.toByteArray()))
        val showing = TestFixtureData.showings.first()

        // when : 좌석을 확인한 뒤 좌석 번호를 입력하면
        val exception = assertThrows<IllegalArgumentException> {
            controller.chooseSeat(showing)
        }

        // then : 예외가 발생한다.
        assertEquals("해당 좌석은 이미 예약되었습니다.", exception.message)
    }

    @Test
    fun `예약되지 않고 존재하는 좌석이면 해당 좌석을 반환한다`() {
        // given : 좌석 번호를 입력한다.
        val input = "B1"
        System.setIn(ByteArrayInputStream(input.toByteArray()))
        val showing = TestFixtureData.showings.first()

        // when : 좌석을 확인한 뒤 좌석 번호를 입력하면
        val result = controller.chooseSeat(showing)

        // then : 좌석 리스트가 반환된다.
        assertEquals(listOf(TestFixtureData.seats.seats[2]), result.seats)
    }
}
