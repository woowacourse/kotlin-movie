package domain

import domain.seat.SeatNumber
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class SeatNumberTest {
    @Test
    fun `행이 알파벳 범위를 벗어나면 예외가 발생한다`() {
        // given & when & then
        assertThrows(IllegalArgumentException::class.java) {
            SeatNumber('Z', 1)
        }
    }

    @Test
    fun `열이 1부터 4 이외의 범위면 예외가 발생한다`() {
        // given & when & then
        assertThrows(IllegalArgumentException::class.java) {
            SeatNumber('A', 5)
        }
    }

    @Test
    fun `유효한 행과 열로 SeatNumber가 생성된다`() {
        // given & when
        val actual = SeatNumber('A', 1)

        // then
        assertThat(actual.toString()).isEqualTo("A1")
    }
}
