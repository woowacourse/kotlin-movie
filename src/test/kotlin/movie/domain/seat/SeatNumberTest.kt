package movie.domain.seat

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class SeatNumberTest {
    @Test
    fun `올바른 행과 열이라면 좌석 번호 객체가 생성된다`() {
        assertDoesNotThrow {
            SeatNumber(row = 'A', col = 1)
        }
    }

    @Test
    fun `좌석 열이 A에서 Z 사이의 알파벳이 아니면 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> {
            SeatNumber(row = 'a', col = 1)
        }

        assertThrows<IllegalArgumentException> {
            SeatNumber(row = '!', col = 1)
        }
    }

    @Test
    fun `좌석 번호(Col)가 0이하라면 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> {
            SeatNumber(row = 'A', col = 0)
        }

        assertThrows<IllegalArgumentException> {
            SeatNumber(row = 'A', col = -10)
        }
    }

    @Test
    fun `toString()을 이용하여 좌석 번호 문자열을 가져올 수 있다`() {
        val seatNumber = SeatNumber(row = 'A', col = 1)

        assertThat(seatNumber.toString()).isEqualTo("A1")
    }
}
