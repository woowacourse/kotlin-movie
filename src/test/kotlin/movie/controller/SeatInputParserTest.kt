package movie.controller

import movie.domain.seat.SeatInputParser
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class SeatInputParserTest {
    private val seatInputParser = SeatInputParser()

    @Test
    fun `좌석 문자열을 좌석 위치 목록으로 변환한다`() {
        val positions = seatInputParser.parse("A1, C2")

        assertThat(positions.display()).isEqualTo("A1, C2")
    }

    @Test
    fun `좌석 문자열 형식이 잘못되면 예외를 던진다`() {
        assertThatThrownBy {
            seatInputParser.parse("A1, Cx")
        }.isInstanceOf(IllegalArgumentException::class.java)
    }
}


