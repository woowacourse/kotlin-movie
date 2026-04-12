@file:Suppress("NonAsciiCharacters")

package model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class PointTest {
    @Test
    fun `포인트가 음수이면 예외가 발생한다`() {
        // given & when & then
        assertThrows(IllegalArgumentException::class.java) {
            Point(-1)
        }
    }

    @Test
    fun `포인트를 돈으로 변환할 수 있다`() {
        // given
        val value = 500
        val expect = Money(value)
        val point = Point(value)

        // when
        val actual = point.convertToMoney()

        // then
        assertThat(actual).isEqualTo(expect)
    }
}
